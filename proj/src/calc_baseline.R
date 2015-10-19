rm(list=ls())

# Output baseline by combining distance and fitness scores across 40 levels and averaging across trials

library(xtable)

wd <- ''
outdir <- paste0(wd, 'writeup/')
datadir <- paste0(wd, 'data/')


for(i in 1:40){
  dist_file <- paste0(datadir, 'distance',i)
  fit_file <- paste0(datadir, 'fitnessScores',i)
  if(i == 1){
    distance <- read.table(dist_file)
    fitness <- read.table(fit_file)
  }else{
    distance <- cbind(distance, read.table(dist_file))
    fitness <- cbind(fitness, read.table(fit_file))
  }
}

dist_sum <- rowSums(distance)
fit_sum <- rowSums(fitness)

print(paste("Average Distance Sum:",mean(dist_sum)))
print(paste("Average Fitness Sum:",mean(fit_sum)))