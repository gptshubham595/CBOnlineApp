package com.codingblocks.cbonlineapp.database.models

import androidx.room.*
import com.codingblocks.cbonlineapp.database.ListObject

@Entity(
    indices = [Index("run_id")],
    foreignKeys = [(ForeignKey(
        entity = RunModel::class,
        parentColumns = ["crUid"],
        childColumns = ["run_id"],
        onDelete = ForeignKey.CASCADE // or CASCADE
    ))]
)
data class SectionModel(
    @PrimaryKey
    var csid: String,
    var name: String,
    var sectionOrder: Int,
    var premium: Boolean,
    var status: String,
    var run_id: String,
    var attemptId: String
) : ListObject() {
    @Ignore
    override fun getType(): Int = TYPE_SECTION

    @Ignore
    var idList = ArrayList<String>()
    @Ignore
    var totalContent = 0
    @Ignore
    var completedContent = 0
    @Ignore
    var totalTime = 0L

}
