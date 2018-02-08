package edu.knoldus.services

import com.typesafe.config.{Config, ConfigFactory}
import twitter4j.auth.AccessToken
import twitter4j._

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import edu.knoldus.entity.Tweets

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
    */
  def getTweets(hashTag: String): Future[List[Tweets]] = Future {
      val query: Query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList: QueryResult = twitter.search(query)
      val tweets = responseList.getTweets.asScala.toList
      val allTweets = tweets.map {
        tweet =>
          Tweets(tweet.getText, tweet.getUser.getScreenName, tweet.getCreatedAt)
      }
    allTweets.sortBy(tweet => tweet.date)
  }

  /**
    * this method counts number of feeds
    * on basis of a hash tag
    */

  def getNumberOfTweets(hashTag: String): Future[Int] = Future {
      val query: Query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList: QueryResult = twitter.search(query)
      val tweets = responseList.getTweets.asScala.toList
      tweets.size
  }
  /**
    * this method returns
    * average tweets per day
    */
  def getAverageTweetsPerDay(hashTag: String): Future[Int] = Future {
      val query: Query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList: QueryResult = twitter.search(query)
      val tweetsList = responseList.getTweets.asScala.toList
      val tweetsMapByDate = tweetsList.groupBy(tweet => tweet.getCreatedAt)
      tweetsList.size / tweetsMapByDate.size

  }

  /**
    * this method returns a touple
    * first member tells average retweets per tweet
    * second member tell average favourites per tweet
    */

  def getAverageReTweetsAndLikes(hashTag: String): Future[(Int, Double)] = Future {
      val query: Query = new Query(hashTag)
      query.setSince("2018-01-20")
      val responseList: QueryResult = twitter.search(query)
      val tweetsList = responseList.getTweets.asScala.toList
      val tweetsListSize = tweetsList.size
      val reTweetsList = tweetsList.map(tweet => tweet.getRetweetCount)
      val favList = tweetsList.map(tweet => tweet.getFavoriteCount)
      (reTweetsList.sum / tweetsListSize, favList.sum / tweetsListSize.toDouble)
  }
}
