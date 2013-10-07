package isucon3.action

import xitrum.annotation.POST
import isucon3.model.DB

@POST("reset")
class Reset extends DefaultLayout {
  def execute() {
    db.reset()
    respondText("done")
  }
}
