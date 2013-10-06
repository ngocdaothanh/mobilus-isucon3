package isucon3.action

import xitrum.annotation.GET

@GET("")
class Index extends DefaultLayout {
  def execute() {
    respondView()
  }
}
