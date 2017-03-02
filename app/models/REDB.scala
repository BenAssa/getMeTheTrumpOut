package models

import java.sql.{Connection, DriverManager}

/**
  * Created by ben on 12/5/16.
  */
object REDB {
  var language="Ru"
  var defaultDB="realJp"


  var initilized: Boolean=false
  var c:Connection= null
  var SCOREBOARD_LIMIT=15;
  var STARTING_SENTENCE_ID=2;
  var Lang=""
  def initilize( test:Boolean):Boolean = {
    if (test) {
      println("connecting to test server")

      c = DriverManager.getConnection("jdbc:postgresql://ec2-52-45-222-154.compute-1.amazonaws.com:5432/d34smc9ru3oa4c?sslmode=require",
        "uva37jrendjq5", "p1ea08fbc25005235a33f6c865c61bfec98bfd1e9f5d36cb540a607e5b20fee87");
      return true

    }
    else
    {

      println("connecting to main server")

      c = DriverManager.getConnection("jdbc:postgresql://ec2-34-200-207-194.compute-1.amazonaws.com:5432/dacuqf64b817r5?sslmode=require",
        "u5mqaghkdjalvn", "p87489ba259cf688a956a95571371911fbbd5bc7e7bc61fd144243c9db81388cd");
      return true
    }




  }
}
