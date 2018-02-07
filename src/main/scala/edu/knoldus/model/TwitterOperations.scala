package edu.knoldus.model

import java.util.Date

import com.typesafe.config.{Config, ConfigFactory}
import twitter4j.auth.AccessToken
import twitter4j.{Query, Twitter, TwitterException, TwitterFactory}
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class MyTweets(tweetText: String, userName: String, date: Date)

class TwitterOperations {

  val twitter: Twitter = new TwitterFactory().getInstance()
  val configurationLoader: Config = ConfigFactory.load()
  val consumerKey: String = configurationLoader.getString("twitter.consumer.key")
  val consumerSecret: String = configurationLoader.getString("twitter.consumer.secret")
  val accessToken: String = configurationLoader.getString("twitter.access.token")
  val accessTokenSecret: String = configurationLoader.getString("twitter.access.tokenSecret")
  twitter.setOAuthConsumer(consumerKey, consumerSecret)
  twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret))

  /**
    * this method gives
    * total tweets
    *
    * @param hashTag
    * @return
    */
  def getTweets(hashTag: String): Future[List[MyTweets]] = Future {
    try {
      val query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList = twitter.search(query)
      val tweets = responseList.getTweets.asScala.toList
      val allTweets = tweets.map {
        tweet =>
          MyTweets(tweet.getText, tweet.getUser.getScreenName, tweet.getCreatedAt)
      }
      allTweets.sortBy(_.date)
    }
    catch {
      case exception: TwitterException => List[MyTweets]()
    }
  }

  /**
    * this method counts number of feeds
    * on basis of a hash tag
    */

  def getNumberOfTweets(hashTag: String): Future[Int] = Future {
    try {
      val query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList = twitter.search(query)
      val tweets = responseList.getTweets.asScala.toList
      tweets.size
    }
    catch {
      case exception: Exception => 0
    }
  }

  def getAverageTweetsPDay(hashTag: String): Future[Int] = Future {
    try {
      val query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList = twitter.search(query)
      val tweetsList = responseList.getTweets.asScala.toList
      val tweetsMapByDate = tweetsList.groupBy(tweet => tweet.getCreatedAt)
      tweetsList.size / tweetsMapByDate.size
    }
    catch {
      case exception: Exception => 0
    }
  }

  /**
    * this method returns a touple
    * first member tells average retweets per tweet
    * second member tell average favourites per tweet
    */

  def getAverageReTweetsAndLikes(hashTag: String): Future[(Int, Double)] = Future {
    try {
      val query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList = twitter.search(query)
      val tweetsList = responseList.getTweets.asScala.toList
      val feedsListSize = tweetsList.size
      val reTweetsList = tweetsList.map(tweet => tweet.getRetweetCount)
      val favList = tweetsList.map(tweet => tweet.getFavoriteCount)
      print(s"\n\n reTweetlist: $reTweetsList")
      print(s"\n\n  fav list: $favList")
      (reTweetsList.sum / feedsListSize, favList.sum / feedsListSize.toDouble)
    }
    catch {
      case exception: Exception => (0, 0)
    }
  }
}
