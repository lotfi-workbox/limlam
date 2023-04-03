package com.saeedlotfi.limlam.data.database

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration


class DbMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var oVersion = oldVersion
        if (oVersion == 0L && newVersion == 1L) {
            realm.schema.create("Theme")
                .addField("name", String::class.java, FieldAttribute.REQUIRED)
                .addField("colorAccent", String::class.java, FieldAttribute.REQUIRED)
                .addField("backgrounds", String::class.java, FieldAttribute.REQUIRED)
                .addField("windowBackground", String::class.java, FieldAttribute.REQUIRED)
                .addField("paleBackgrounds", String::class.java, FieldAttribute.REQUIRED)
                .addField("textsAndIcons", String::class.java, FieldAttribute.REQUIRED)
                .addField("textHint", String::class.java, FieldAttribute.REQUIRED)
                .addField("strokeColor", String::class.java, FieldAttribute.REQUIRED)
                .addField("topOfWindows", String::class.java, FieldAttribute.REQUIRED)
                .addField("undoBar", String::class.java, FieldAttribute.REQUIRED)
                .addField("startShadow", String::class.java, FieldAttribute.REQUIRED)
                .addField("endShadow", String::class.java, FieldAttribute.REQUIRED)
                .addField("rippleColor", String::class.java, FieldAttribute.REQUIRED)
                .addField("id", Long::class.java, FieldAttribute.PRIMARY_KEY)
            realm.schema.create("UserSettingModel")
                .addField("userName", String::class.java, FieldAttribute.REQUIRED)
                .addField("nightMode", Boolean::class.java, FieldAttribute.REQUIRED)
                .addRealmObjectField("lightTheme",realm.schema.get("Theme")!!)
                .addRealmObjectField("darkTheme",realm.schema.get("Theme")!!)
                .addField("id", Long::class.java, FieldAttribute.PRIMARY_KEY)
            oVersion++
        }
        if (oVersion == 1L && newVersion == 2L) {
            realm.schema.get("Theme")
                ?.removeField("colorAccent")
                ?.addField("topOfWindowTexts",String::class.java,FieldAttribute.REQUIRED)
            realm.where("Theme").findAll()?.map {
                it.set("topOfWindowTexts","#FFFFFF")
            }
        }
    }

    override fun hashCode(): Int {
        return DbMigration::class.java.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is DbMigration
    }

}