library(ggplot2)
library(reshape2)
library(dplyr)
library(grid)
library(scales)

rm(list=ls())

# Directory Structure
wd <- '~/Documents/Stanford/CS221/mario/marioai/'
proj_dir <- paste0(wd,'proj/')
data_dir <- paste0(proj_dir, 'data/paramTuning/eps_test/stats/')
out_dir  <- paste0(proj_dir, 'writeup/imgs/')
src_dir  <- paste0(proj_dir, 'writeup/graphScripts/')

source(paste0(src_dir, 'formats.R'))

# hyperparameters
epoch_size <- 1

qlinear_dir <- paste0(data_dir, '')
qlearning_dir <- paste0(data_dir, '')
nn_dir <- paste0(data_dir, '')


import_data <- function(dir_path, measure, agent){
  data <- read.csv(paste0(dir_path, measure, '_', agent), header=FALSE)
  colnames(data) <- measure
  data$agent <- agent
  data$iter <- 1:nrow(data)
  return(data)
}


################ Plot distance traveled graph ####################
measure <- 'distance'

nn_agent       <- import_data(nn_dir, measure, 'NNAgent')
qlinear_agent   <- import_data(qlinear_dir, measure,'QLinearAgent')
qlearning_agent <- import_data(qlearning_dir, measure,'QLearningAgent')
qlearning_agent$agent <- 'IdentityAgent'

data <- rbind(qlinear_agent, qlearning_agent, nn_agent)
#data <- rbind(nn_agent)
data$epoch <- floor(data$iter / epoch_size)

# Create Line Chart

# convert factor to numeric for convenience 
Orange$Tree <- as.numeric(Orange$Tree) 
ntrees <- max(Orange$Tree)

# set up the plot 
plot(xrange, yrange, type="n", xlab="Age (days)",
     ylab="Circumference (mm)" ) 
colors <- rainbow(ntrees) 
linetype <- c(1:ntrees) 
plotchar <- seq(18,18+ntrees,1)

# add lines 
for (i in 1:ntrees) { 
  tree <- subset(Orange, Tree==i) 
  lines(tree$age, tree$circumference, type="b", lwd=1.5,
        lty=linetype[i], col=colors[i], pch=plotchar[i]) 
} 

# add a title and subtitle 
title("Tree Growth", "example of line plot")

# add a legend 
legend(xrange[1], yrange[2], 1:ntrees, cex=0.8, col=colors,
       pch=plotchar, lty=linetype, title="Tree")

# Line Graph
#line_data <- summarise(group_by(data, epoch, agent), mdist=mean(distance))
#g <- ggplot(line_data, aes(x=epoch, y=mdist, colour=agent)) + geom_line() +
#  labs(x='Training Epochs', y='Distance Traveled', colour='Agent')

#pdf(paste0(out_dir, 'dist_line.pdf'))
#plot(common_format(g))
#dev.off()

# Bar Graph of average
# Look only at last few epochs
data_last <- subset(data, epoch >= 18)
bar_data <- summarise(group_by(data_last, agent),mdist=mean(distance))
g <- ggplot(bar_data, aes(x=agent, y=mdist)) + geom_bar(stat='identity') +
  labs(x='Agent', y='Distance Traveled')
pdf(paste0(out_dir, 'dist_bar.pdf'))
plot(common_format(g))
dev.off()