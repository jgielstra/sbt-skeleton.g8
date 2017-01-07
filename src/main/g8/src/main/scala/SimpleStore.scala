package $organization$.$name;format="lower,word"$

class SimpleStore[T] {
    private var store = List[T]()   //<< Side effect ...
    def put(item: T):Unit = store = store :+ item
    def get(id: Int): Option[T] = store.lift(id)
    def getAll: List[T] = store
    def putAll(l: List[T]):Unit =  store ++= l
}
