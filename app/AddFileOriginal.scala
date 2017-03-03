import java.sql.{Connection, Statement}
import java.util.{ArrayList, List}

import models.{LoadFromCSV, REDB, TableDef}

/**s
  * Created by ben on 12/20/16.
  */



object AddFileOriginal {

  def main(args: Array[String]): Unit = {
    var c: Connection = null
    val problemRows: List[String] = new ArrayList[String]
    REDB.initilize(false)
    c = REDB.c
    val filename="train.csv"
    val tableDef: TableDef = new TableDef
    tableDef.targetTable = "sentiment_all"
    var stmt: Statement = null
    stmt = c.createStatement
    println(c)


    tableDef.cols.put(0,new TableDef.ColDef( false, "val", false))

    tableDef.cols.put(5,new TableDef.ColDef( true, "sentence", false))

    tableDef.colNum=6
    val loadFromCSV: LoadFromCSV = new LoadFromCSV(tableDef, c, problemRows)
    loadFromCSV.run(filename)


    println(c)
  }
}
