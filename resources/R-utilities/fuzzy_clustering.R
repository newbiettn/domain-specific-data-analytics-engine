normalization <- function(x) {
  (x - min(x, na.rm=TRUE))/(max(x,na.rm=TRUE) - min(x, na.rm=TRUE))
}
mf <- read.csv("mf.csv", header = FALSE)
normalized.mf <- apply(mf, 2, normalization)
