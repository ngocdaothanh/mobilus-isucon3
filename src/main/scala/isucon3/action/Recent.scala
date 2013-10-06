package isucon3.action

import xitrum.annotation.GET

@GET("recent/:page")
class Recent extends DefaultLayout {
  def execute() {
    respondView()
  }
}
