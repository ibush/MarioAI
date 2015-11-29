library(ggplot2)
library(reshape2)
library(dplyr)

rm(list=ls())

# Directory Structure
wd <- '~/Desktop/MarioAI/'
proj_dir <- paste0(wd,'proj/')
#data_dir <- paste0(proj_dir, 'data/')
data_dir <- paste0(wd, 'bin/stats/')
out_dir  <- paste0(proj_dir, 'writeup/imgs/')

# Plot weight stats of fully connected layer
plot_fc <- function(weight_stats, name){
  fc <- subset(weight_stats, lname==name)
  fc$lname <- NULL
  fc$iter <- 1:nrow(fc)
  fc <- melt(fc, id.vars='iter')
  g <- ggplot(fc, aes(x=iter, y=value,colour=variable)) + geom_line()
  plot(g)
}


metric_stats <- read.csv(paste0(data_dir, 'NNmetrics_NNAgent'))
weight_stats <- read.csv(paste0(data_dir, 'NNweights_NNAgent'))

weight_stats$X <- NULL
names(weight_stats) <- c('lname','vmean','variance','vmin','vmax')

plot_fc(weight_stats, 'fc2')

# Plot error
names(metric_stats) <- c('stat','value')
error_stats <- subset(metric_stats, stat=='error')
error_stats$iter <- 1:nrow(error_stats)
g <- ggplot(error_stats, aes(x=iter,y=value)) + geom_line()
plot(g)