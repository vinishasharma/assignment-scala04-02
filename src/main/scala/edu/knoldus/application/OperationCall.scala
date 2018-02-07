package edu.knoldus.application

import edu.knoldus.model.{TwitterOperations}
import org.apache.log4j.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object OperationCall {

  def main(args: Array[String]): Unit = {

    val log = Logger.getLogger(this.getClass)
    val tweetOperation = new TwitterOperations
    val hashTag = "#Apple"
    val tweets = tweetOperation.getTweets(hashTag).map(tweetResult => log.info(s"\n\n\n$tweetResult\n\n\n"))
    val numberOfTweets = tweetOperation.getNumberOfTweets(hashTag).map(tweetResult => log.info(s"\n\n\n\nnumber of tweets: $tweetResult\n\n\n"))
    Thread.sleep(1500)
    val averageTweets = tweetOperation.getAverageTweetsPDay(hashTag).map(tweetResult => log.info(s"\n\n\n\n Average Tweets: $tweetResult"))
    val averageReTweetAndLikes = tweetOperation.getAverageReTweetsAndLikes(hashTag).
      map(tweetResult => log.info(s"\n\n\n\naverage retweets and likes:$tweetResult\n\n\n"))
    Thread.sleep(1500)
  }
}

