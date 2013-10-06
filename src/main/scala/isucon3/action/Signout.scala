package isucon3.action

import xitrum.Action
import xitrum.annotation.POST

@POST("signout")
class Signout extends DefaultLayout with RequireUser with AntiCsrf {
  def execute() {
    session.clear()
    redirectTo[Index]()
  }
}
