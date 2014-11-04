package mg


package object domain {

  implicit class RichMap[K, V](m: collection.immutable.Map[K, V]) {

    def adjust(k: K)(f: V => V) = m.get(k).fold(m)(b => m.updated(k, f(b)))
  }


}
