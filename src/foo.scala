import java.util.concurrent.{Executor, Executors, TimeUnit}

object foo extends App {
  val cfg = Config.fromArgs(args)
  def doStuff(): Unit = {
    dataLinkRecieve
    println(System.currentTimeMillis())
  }
  val endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cfg.duration)
  val scheduler = Executors.newScheduledThreadPool(1)
  val proc: Runnable = new Runnable {
    def run: Unit = {
      doStuff()
      if (System.currentTimeMillis() < endTime) scheduler.schedule(proc, 1, TimeUnit.SECONDS)
      else scheduler.shutdown()
    }
  }
  proc.run()
}

case class Config(id: Int, duration: Int, dest: Int, msg: Option[String], neighbors: Seq[Int])

object Config {
  def fromArgs(args: Array[String]) = {
    val (msg, neighbors) =
      if (new java.util.Scanner(args(3)).hasNextInt()) (None, args.drop(3).map(_.toInt))
      else (Option(args(3)), args.drop(4).map(_.toInt))
    Config(
      id = args(0).toInt,
      duration = args(1).toInt,
      dest = args(2).toInt,
      msg = msg,
      neighbors = neighbors
    )
  }
}
