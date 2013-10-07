package isucon3.action

import xitrum.annotation.POST

@POST("reset")
class Reset extends DefaultLayout {
  def execute() {
    db.reset()
    removeActionCache()
    respondText("done")
  }
}
