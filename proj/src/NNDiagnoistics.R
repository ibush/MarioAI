library(ggplot2)
library(reshape2)
library(dplyr)
library(zoo)

rm(list=ls())

# Directory Structure
wd <- '~/Desktop/MarioAI/'
proj_dir <- paste0(wd,'proj/')
data_dir <- paste0(wd, 'bin/stats/')
out_dir  <- paste0(proj_dir, 'stats/')

epoch_size <- 1000

# Plot weight stats of fully connected layer
plot_fc <- function(weight_stats, name, out_dir){
  fc <- subset(weight_stats, lname==name)
  fc$lname <- NULL
  fc$iter <- 1:nrow(fc)
  fc <- melt(fc, id.vars='iter')
  
  pdf(paste0(out_dir, name, '_w.pdf'))
  fc_mv <- subset(fc, variable=='vmean' | variable=='variance')
  g <- ggplot(fc_mv, aes(x=iter, y=value,colour=variable)) + geom_line() +
    ggtitle(paste(name, 'Weight Statistics'))
  plot(g)
  
  fc_mm <- subset(fc, variable=='vmax' | variable=='vmin')
  g <- ggplot(fc_mm, aes(x=iter, y=value,colour=variable)) + geom_line() +
    ggtitle(paste(name, 'Weight Statistics'))
  plot(g)
  dev.off()
}


# Import data
metric_stats <- read.csv(paste0(data_dir, 'NNmetrics_NNAgent'), header=F)
weight_stats <- read.csv(paste0(data_dir, 'NNweights_NNAgent'), header=F)
dist_stats <- read.csv(paste0(data_dir, 'distance_NNAgent'), header=F)
fit_stats <- read.csv(paste0(data_dir, 'fitnessScores_NNAgent'), header=F)


# Print weight statistics
weight_stats$X <- NULL
names(weight_stats) <- c('lname','vmean','variance','vmin','vmax')

all_layers <- paste(unique(weight_stats$lname))
for(l in all_layers){
  if(substr(l,1,2) == 'fc'){
    print(paste('Generating weight statistics graph for :',l))
    plot_fc(weight_stats, l, out_dir)
  }
}


# Plot error
names(metric_stats) <- c('stat','value')
error_stats <- subset(metric_stats, stat=='error')
error_stats$iter <- 1:nrow(error_stats)
error_stats$value <- abs(error_stats$value)
error_stats$epoch <- floor(error_stats$iter / epoch_size)
error_stats <- summarise(group_by(error_stats, epoch), value=mean(value))
g <- ggplot(error_stats, aes(x=epoch,y=value)) + geom_bar(stat='identity') +
    labs(x='Epoch',y='Error') + ggtitle('Neural Network Error')
pdf(paste0(out_dir, 'NNError.pdf'))
plot(g)
dev.off()


# Plot distance traveled
names(dist_stats) <- c('dist')
dist_stats$sim_num <- 1:nrow(dist_stats)
dist_stats$rolldist <- rollmeanr(dist_stats$dist,10,fill=NA)
g <- ggplot(dist_stats, aes(x=sim_num, y=rolldist)) + geom_line()
pdf(paste0(out_dir, 'NNDist.pdf'))
plot(g)
dev.off()

# Plot Fitness Score
names(fit_stats) <- c('score')
fit_stats$sim_num <- 1:nrow(fit_stats)
fit_stats$rollscore <- rollmeanr(fit_stats$score, 10, fill=NA)
g <- ggplot(fit_stats, aes(x=sim_num, y=rollscore)) + geom_line()
pdf(paste0(out_dir, 'NNFit.pdf'))
plot(g)
dev.off()