package github.keylity.side

/**
 * SPI for SideContext
 */
trait ContextLike {
  def get(key: String): Option[Any]
  def set(key: String, value: Any): Option[Any]
}
