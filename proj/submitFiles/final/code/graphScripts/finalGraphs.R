library(ggplot2)
library(reshape2)
library(dplyr)
library(grid)
library(scales)

rm(list=ls())

# Directory Structure
wd <- '~/Desktop/MarioAI/'
proj_dir <- paste0(wd,'proj/')
data_dir <- paste0(proj_dir, 'data/')
out_dir  <- paste0(proj_dir, 'writeup/imgs/')
src_dir  <- paste0(proj_dir, 'writeup/graphScripts/')

source(paste0(src_dir, 'formats.R'))

# hyperparameters
epoch_size <- 100

qlinear_dir <- paste0(data_dir, 'QLinearAgent/')
random_dir  <- paste0(data_dir, 'RandomAgent/')
qlearning_dir <- paste0(data_dir, 'QLearningAgent/')
nn_dir <- paste0(data_dir, 'NNAgent/')


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
random_agent    <- import_data(random_dir, measure,'RandomAgent')
qlearning_agent <- import_data(qlearning_dir, measure,'QLearningAgent')
qlearning_agent$agent <- 'IdentityAgent'
qlinear_agent$agent <- 'LinearAgent'

data <- rbind(qlinear_agent, random_agent, qlearning_agent, nn_agent)
data$epoch <- floor(data$iter / epoch_size)
data <- subset(data, epoch != 20) #there is only one observation in this bin

# Line Graph
line_data <- summarise(group_by(data, epoch, agent), mdist=mean(distance))
g <- ggplot(line_data, aes(x=epoch, y=mdist, colour=agent)) + geom_line() +
  labs(x='Training Epochs', y='Distance Traveled', colour='Agent')

pdf(paste0(out_dir, 'dist_line.pdf'))
plot(common_format(g))
dev.off()

# Bar Graph of average
# Look only at last few epochs
data_last <- subset(data, epoch >= 18)
bar_data <- summarise(group_by(data_last, agent),mdist=mean(distance))
g <- ggplot(bar_data, aes(x=agent, y=mdist)) + geom_bar(stat='identity') +
  labs(x='Agent', y='Distance Traveled')
pdf(paste0(out_dir, 'dist_bar.pdf'))
plot(common_format(g))
dev.off()

############### Runtime Analysis ###############################

measure <- 'time'
  
nn_agent       <- import_data(nn_dir, measure, 'NNAgent')
qlinear_agent   <- import_data(qlinear_dir, measure,'QLinearAgent')
random_agent    <- import_data(random_dir, measure,'RandomAgent')
qlearning_agent <- import_data(qlearning_dir, measure,'QLearningAgent')
qlearning_agent$agent <- 'IdentityAgent'
qlinear_agent$agent <- 'LinearAgent'
  
data <- rbind(qlinear_agent, random_agent, qlearning_agent, nn_agent)
data <- summarise(group_by(data, agent), rt = mean(time/1000000))

g <- ggplot(data, aes(x=agent, y=rt)) + geom_bar(stat='identity') +
  labs(x='Agent',y='Runtime Per Iteration (ms)')

pdf(paste0(out_dir, 'runtime_bar.pdf'))
plot(common_format(g))
dev.off()

################## Parameter convergence ############################

qlinear_agent <- read.csv(paste0(qlinear_dir, 'weightsStats_QLinearAgent'), header=FALSE)
colnames(qlinear_agent) <- c('min','max','mean')
qlinear_agent$iter <- 1:nrow(qlinear_agent)
qlinear_agent <- melt(qlinear_agent, id.vars='iter')

data <- qlinear_agent
data$epoch <- floor(data$iter / epoch_size)
line_data <- summarise(group_by(data, epoch, variable), weight=mean(value))
g <- ggplot(line_data, aes(x=epoch, y=weight, colour=variable)) + geom_line()
plot(g)