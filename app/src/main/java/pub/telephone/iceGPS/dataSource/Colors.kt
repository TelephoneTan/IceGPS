package pub.telephone.iceGPS.dataSource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

private typealias ColorValue = Int
private typealias ColorSet = Colors<ColorValue>

private interface Value

@JvmInline
private value class CV(val color: Color) : Value

private class C(vararg pairs: Pair<Mode, Value>) :
    Selector<Mode, CV, ColorValue>(Mode.fallback, mapOf(*pairs), { it.color.toArgb() }), Value

data class ColorsMyActivity<T>(
    @JvmField var background: T,
) : Config<ColorsMyActivity<T>> {
    override fun copy(): ColorsMyActivity<T> {
        return copy(background = background)
    }
}

data class ColorsMain<T>(
    @JvmField var text: T,
) : Config<ColorsMain<T>> {
    override fun copy(): ColorsMain<T> {
        return copy(text = text)
    }
}

data class Colors<T>(
    @JvmField var myActivity: ColorsMyActivity<T>,
    @JvmField var main: ColorsMain<T>,
) : Config<Colors<T>> {
    override fun copy(): Colors<T> {
        return copy(myActivity = myActivity)
    }
}

private enum class Mode {
    DEFAULT,
    NIGHT;

    object Palette {
        val black = C(
            DEFAULT to CV(Color(0xff0a0101)),
            NIGHT to CV(Color(0xffff78fd)),
        )
        val red = C(
            DEFAULT to CV(Color(0xffffe478)),
            NIGHT to CV(Color(0xff47c55f)),
        )
    }

    companion object {
        val fallback = DEFAULT
        val template = Colors(
            myActivity = ColorsMyActivity(
                background = Palette.black
            ),
            main = ColorsMain(
                text = Palette.red
            ),
        )
    }

    fun calc(): ColorSet = Selector.transform(template.copy(), this)
}

object ColorManager {
    @Volatile
    private var night: Boolean? = null

    @Volatile
    var current = Mode.fallback.calc()

    fun commit(night: Boolean) {
        if (night == this.night) {
            return
        }
        when (night) {
            true -> Mode.NIGHT
            false -> Mode.DEFAULT
        }.calc().also {
            synchronized(this) {
                if (night == this.night) {
                    return
                }
                this.night = night
                current = it
                DataNodeManager.DataNodeColor.CallOnAll { node ->
                    node.EmitChange_ui(mutableSetOf(node.Color.SetResult(it)))
                    null
                }
            }
        }
    }
}