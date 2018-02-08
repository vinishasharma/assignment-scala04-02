package edu.knoldus.entity

import java.util.Date

case class Tweets (tweetText: String, userName: String, date: Date) {

  override def toString: String = {
    s"Tweet text: $tweetText \nusername : $userName \nDate: $date\n"
  }
}
