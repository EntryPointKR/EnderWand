package kr.entree.enderwand.data

import java.io.File

/**
 * Created by JunHyung Lim on 2020-01-10
 */
infix fun String.childOf(parent: File) = File(parent, this)