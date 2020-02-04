package kr.entree.enderwand.reactor

/**
 * Created by JunHyung Lim on 2019-12-21
 */
class SimpleReactor<T> : Reactor<T> {
    private val actors = mutableSetOf<Actor<T>>()

    override fun subscribe(actor: Actor<T>) = actors.add(actor)

    override fun remove(actor: Actor<T>) = actors.remove(actor)

    override fun notify(value: T) {
        val iterator = actors.iterator()
        while (iterator.hasNext()) {
            val actor = iterator.next()
            val context = ReactorContext(value)
            actor(context)
            if (context.remove) {
                iterator.remove()
            }
        }
    }
}