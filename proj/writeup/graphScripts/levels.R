library(ggplot2)
library(dplyr)

source(paste0(src_dir, 'formats.R'))

# Directory Structure
wd <- '~/Documents/Stanford/CS221/mario/marioai/'
proj_dir <- paste0(wd,'proj/')
data_dir <- paste0(proj_dir, 'data/allLevels/')
out_dir  <- paste0(proj_dir, 'writeup/imgs/')
src_dir  <- paste0(proj_dir, 'writeup/graphScripts/')

agents <- c('RandomAgent', 'QLearningAgent', 'QLinearAgent', 'NNAgent')

all_data <- data.frame()
for(agent in agents){
  agent_data <- data.frame()
  for(i in 0:39){
    data_path <- paste0(data_dir, agent,'/level_', i, '/stats/')
    data <- read.csv(paste0(data_path, 'distance_', agent), header=FALSE)
    names(data) <- 'dist'
    data$sim_num <- 1:nrow(data)
    data$level <- i
    data$agent <- agent
    if(agent == 'QLearningAgent') data$agent <- 'IdentityAgent'
    agent_data <- rbind(agent_data, data)
  }
  min_ind <- 1950 # store last 50 runs
  if(agent == 'RandomAgent' || agent == 'QLearningAgent') min_ind <- 200
  agent_data <- subset(agent_data, sim_num > min_ind)
  all_data <- rbind(all_data, agent_data)
}

sum_data <- summarise(group_by(all_data, agent, level), dist=mean(dist))

# Print sum of scores for all levels
level_sum <- summarise(group_by(sum_data, agent), dist=sum(dist))
print(level_sum)
ten_level_data <- subset(sum_data, level < 10)
ten_level_sum <- summarise(group_by(ten_level_data, agent), dist=sum(dist))
print(ten_level_sum)

# scatter plot
g <- ggplot(sum_data, aes(x=level, y=dist, colour=agent)) + geom_point()+
  labs(x='Training Epochs', y='Distance Traveled', colour='Agent')
pdf(paste0(out_dir, 'dist_levels.pdf'))
plot(common_format(g))
dev.off()