package edu.knoldus.application

import edu.knoldus.services.TwitterOperations
import org.apache.log4j.Logger
import scala.concurrent.ExecutionContext.Implicits.global

object OperationCall {

  def main(args: Array[String]): Unit = {

    val log = Logger.getLogger(this.getClass)
    val tweetOperation: TwitterOperations = new TwitterOperations
    val hashTag: String = "#apple"
    tweetOperation.getTweets(hashTag).map(tweetResult => log.info(s"\n\n\n$tweetResult\n\n\n"))
    tweetOperation.getNumberOfTweets(hashTag).map(tweetResult => log.info(s"\n\n\n\nnumber of tweets: $tweetResult\n\n\n"))
    Thread.sleep(1500)
    tweetOperation.getAverageTweetsPerDay(hashTag).map(tweetResult =>
      log.info(s"\n\n\n\n Average Tweets: $tweetResult\n\n\n\n"))
    tweetOperation.getAverageReTweetsAndLikes(hashTag).
      map(tweetResult => log.info(s"\n\n\n\naverage reTweets and likes:$tweetResult\n\n\n"))
    Thread.sleep(3000)
  }
}

