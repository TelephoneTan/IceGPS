package pub.telephone.iceGPS.dataSource

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

private typealias ColorValue = Int
typealias ColorSet = Colors<ColorValue>

private interface Value

@JvmInline
private value class CV(val color: Color) : Value

private class C(vararg pairs: Pair<Mode, Value>) :
    Selector<Mode, CV, ColorValue>(Mode.fallback, mapOf(*pairs), { it.color.toArgb() }), Value

data class ColorsMyActivity<T>(
    @JvmField var background: T,
    @JvmField var text: T,
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
            DEFAULT to CV(Color(0xff000000)),
        )
        val white = C(
            DEFAULT to CV(Color(0xffffffff)),
        )
    }

    object Common {
        val text = C(
            DEFAULT to Palette.black,
            NIGHT to Palette.white,
        )
    }

    companion object {
        val fallback = DEFAULT
        val template = Colors(
            myActivity = ColorsMyActivity(
                background = C(
                    DEFAULT to Palette.white,
                    NIGHT to CV(Color(0xff1e1e1e)),
                ),
                text = Common.text
            ),
            main = ColorsMain(
                text = Common.text
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