common_format <- function(g){
  #increasing margins to give space between labels and graph
  g <- g+ theme(plot.margin=unit(c(1,1,1,1),"cm"))+
    theme(plot.title= element_text(lineheight=3, size=24, vjust=3))+
    theme(axis.title.y= element_text(vjust=1.5))+
    theme(axis.title.x= element_text(vjust=-1.5))+
    theme(text= element_text(size=15))+
    theme(axis.line = element_line(colour = "black"),
          panel.grid.major = element_blank(),
          panel.grid.minor = element_blank(),
          panel.border = element_blank(),
          panel.background = element_blank()) 
  return(g)
}