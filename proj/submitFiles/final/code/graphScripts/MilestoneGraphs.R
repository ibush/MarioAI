library(ggplot2)
library(reshape2)
library(dplyr)

rm(list=ls())

# Directory Structure
wd <- '~/Desktop/MarioAI/'
proj_dir <- paste0(wd,'proj/')
data_dir <- paste0(proj_dir, 'data/')
out_dir  <- paste0(proj_dir, 'writeup/imgs/')

# hyperparameters
epoch_size <- 10

qlinear_dir <- paste0(data_dir, 'QLinearAgent_2000_runs/')
random_dir  <- paste0(data_dir, 'Random_2000_runs/')
qlearning_dir <- paste0(data_dir, 'QLearningAgent_200_runs/')

import_data <- function(dir_path, measure, agent){
  data <- read.csv(paste0(dir_path, measure, '_', agent), header=FALSE)
  colnames(data) <- measure
  data$agent <- agent
  data$iter <- 1:nrow(data)
  return(data)
}


################ Plot distance traveled graph ####################
measure <- 'distance'

qlinear_agent <- import_data(qlinear_dir, measure,'QLinearAgent')
random_agent <- import_data(random_dir, measure,'RandomAgent')
qlearning_agent <- import_data(qlearning_dir, measure,'QLearningAgent')
qlearning_agent$agent <- 'IdentityAgent'

data <- rbind(qlinear_agent, random_agent, qlearning_agent)
data$epoch <- floor(data$iter / epoch_size)
data <- subset(data, epoch <= 20)

# Line Graph
line_data <- summarise(group_by(data, epoch, agent), mdist=mean(distance))
g <- ggplot(line_data, aes(x=epoch, y=mdist, colour=agent)) + geom_line() +
      ggtitle('Learning Convergence') + labs(x='Training Epochs', y='Distance Traveled', colour='Agent')

pdf(paste0(out_dir, 'dist_line.pdf'))
plot(g)
dev.off()

# Bar Graph of average
bar_data <- summarise(group_by(data, agent),mdist=mean(distance))
g <- ggplot(bar_data, aes(x=agent, y=mdist)) + geom_bar(stat='identity') +
      ggtitle('Mean Distance Traveled') + labs(x='Agent', y='Distance Traveled')
pdf(paste0(out_dir, 'dist_bar.pdf'))
plot(g)
dev.off()

#################### Plot Fitness Score ####################

measure <- 'fitnessScores'

qlinear_agent <- import_data(qlinear_dir, measure,'QLinearAgent')
random_agent <- import_data(random_dir, measure,'RandomAgent')
qlearning_agent <- import_data(qlearning_dir, measure,'QLearningAgent')

data <- rbind(qlinear_agent, random_agent, qlearning_agent)
data$epoch <- floor(data$iter / epoch_size)

line_data <- summarise(group_by(data, epoch, agent), fitscore=mean(fitnessScores))
g <- ggplot(line_data, aes(x=epoch, y=fitscore, colour=agent)) + geom_line()

pdf(paste0(out_dir, 'fitscore_line.pdf'))
plot(g)
dev.off()


bar_data <- summarise(group_by(data, agent),fitscore=mean(fitnessScores))
g <- ggplot(bar_data, aes(x=agent, y=fitscore)) + geom_bar(stat='identity') +
  ggtitle('Mean Fitness Score') + labs(x='Agent', y='Fitness Score')

pdf(paste0(out_dir, 'fitscore_bar.pdf'))
plot(g)
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