package com.codingblocks.cbonlineapp.adapters

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.codingblocks.cbonlineapp.R
import com.codingblocks.cbonlineapp.database.models.SectionModel
import com.codingblocks.cbonlineapp.util.Animations.collapse
import com.codingblocks.cbonlineapp.util.Animations.expand
import com.codingblocks.cbonlineapp.util.DownloadStarter
import com.codingblocks.cbonlineapp.util.FileUtils
import com.codingblocks.cbonlineapp.util.OnCleanDialogListener
import com.codingblocks.cbonlineapp.viewmodels.MyCourseViewModel
import kotlinx.android.synthetic.main.item_section.view.*

class SectionDetailsAdapter(
    private var sectionData: ArrayList<SectionModel>?,
    private var activity: LifecycleOwner,
    private var starter: DownloadStarter,
    private var viewModel: MyCourseViewModel
) : RecyclerView.Adapter<SectionDetailsAdapter.CourseViewHolder>() {

    private lateinit var context: Context
    private var premium: Boolean = false
    private lateinit var courseStartDate: String

    private lateinit var arrowAnimation: RotateAnimation

    fun setData(sectionData: ArrayList<SectionModel>, premium: Boolean, crStart: String) {
        this.sectionData = sectionData
        this.premium = premium
        this.courseStartDate = crStart
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        sectionData?.get(position)?.let { holder.bindView(it) }
    }

    override fun getItemCount(): Int {
        return sectionData?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        context = parent.context

        return CourseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_section, parent, false)
        )
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(data: SectionModel) {
            itemView.title.text = data.name
//            viewModel.getContentWithSectionId(data.csid).observer(activity) { courseContent ->
//                val ll = itemView.findViewById<LinearLayout>(R.id.sectionContents)
//                if (ll.visibility == View.VISIBLE) {
//                    ll.removeAllViews()
//                    expand(ll)
//                } else {
//                    ll.removeAllViews()
//                    ll.visibility = View.GONE
//                }
//                itemView.lectures.text = "0/${courseContent.size} Lectures Completed"
//                var duration: Long = 0
//                var sectionComplete = 0
//                for (content in courseContent) {
//
//                    val factory = LayoutInflater.from(context)
//                    val inflatedView =
//                        factory.inflate(R.layout.item_section_detailed_info, ll, false)
//                    val subTitle = inflatedView.findViewById(R.id.textView15) as TextView
//                    val downloadBtn = inflatedView.findViewById(R.id.downloadBtn) as ImageView
//                    val contentType = inflatedView.findViewById(R.id.contentType) as ImageView
//
//                    if (content.progress == "DONE") {
//                        subTitle.textColor = context.resources.getColor(R.color.green)
//                        downloadBtn.setImageDrawable(context.getDrawable(R.drawable.ic_status_done))
//                        sectionComplete++
//                    }
//                    if (content.contentable == "lecture")
//                        duration += content.contentLecture.lectureDuration
//                    else if (content.contentable == "video") {
//                        duration += content.contentVideo.videoDuration
//                    }
//                    itemView.lectureTime.text = duration.getDurationBreakdown()
//
////                    if (!data.premium)
////                        itemView.free.visibility = View.VISIBLE
//                    viewModel.getSectionDownloadlist(data.csid).observer(activity) {
//                        itemView.downloadSectionBtn.apply {
//                            isVisible = it.isNotEmpty()
//                            setOnClickListener {
//                                if (MediaUtils.checkPermission(context)) {
//                                    if ((context as Activity).getPrefs().SP_WIFI) {
//                                        if (NetworkUtils.connectedToWifi(context) == true) {
//                                            itemView.downloadSectionBtn.isEnabled = false
//                                            (itemView.downloadSectionBtn.image as AnimationDrawable).start()
//                                            starter.startSectionDownlod(data.csid)
//                                        } else {
//                                            showconfirmation(
//                                                context,
//                                                "wifi"
//                                            )
//                                        }
//                                    } else {
//                                        itemView.downloadSectionBtn.isEnabled = false
//                                        (itemView.downloadSectionBtn.image as AnimationDrawable).start()
//                                        starter.startSectionDownlod(data.csid)
//                                    }
//                                } else {
//                                    MediaUtils.isStoragePermissionGranted(context)
//                                }
//                            }
//                        }
//                    }
//                    subTitle.text = content.title
//
//                    if (!data.premium || premium && ((courseStartDate.toLong() * 1000) < System.currentTimeMillis())) {
//                        if (sectionComplete == courseContent.size) {
//                            itemView.lectures.text =
//                                "$sectionComplete/${courseContent.size} Lectures Completed"
//                            itemView.lectures.textColor =
//                                context.resources.getColor(R.color.green)
//                        } else {
//                            itemView.lectures.text =
//                                "$sectionComplete/${courseContent.size} Lectures Completed"
//                            itemView.lectures.textColor =
//                                context.resources.getColor(R.color.black)
//                        }
//                        when {
//                            content.contentable == "lecture" -> {
//                                contentType.setImageDrawable(context.getDrawable(R.drawable.ic_lecture))
//                                if (content.contentLecture.lectureUid.isNotEmpty()) {
//                                    ll.addView(inflatedView)
//                                    if (content.contentLecture.isDownloaded == "false" || !FileUtils.checkDownloadFileExists(context, content.contentLecture.lectureId)) {
//                                        downloadBtn.setImageDrawable(null)
//                                        downloadBtn.background =
//                                            context.getDrawable(android.R.drawable.stat_sys_download)
//                                        inflatedView.setOnClickListener {
//                                            if (content.progress == "UNDONE") {
//                                                if (content.progressId.isEmpty())
//                                                    setProgress(
//                                                        content.ccid,
//                                                        content.attempt_id,
//                                                        content.contentable,
//                                                        data.csid,
//                                                        content.contentLecture.lectureContentId
//                                                    )
//                                                else
//                                                    updateProgress(
//                                                        content.ccid,
//                                                        content.attempt_id,
//                                                        content.progressId,
//                                                        "DONE",
//                                                        content.contentable,
//                                                        data.csid,
//                                                        content.contentLecture.lectureContentId
//                                                    )
//                                            }
//                                            it.context.startActivity(
//                                                it.context.intentFor<VideoPlayerActivity>(
//                                                    VIDEO_ID to content.contentLecture.lectureId,
//                                                    RUN_ATTEMPT_ID to content.attempt_id,
//                                                    CONTENT_ID to content.ccid,
//                                                    SECTION_ID to content.section_id,
//                                                    DOWNLOADED to false
//                                                ).singleTop()
//                                            )
//                                        }
//                                        downloadBtn.setOnClickListener {
//                                            if (MediaUtils.checkPermission(context)) {
//                                                if ((context as Activity).getPrefs().SP_WIFI) {
//                                                    if (NetworkUtils.connectedToWifi(context) == true) {
//                                                        startFileDownload(
//                                                            content.contentLecture.lectureId,
//                                                            data.csid,
//                                                            content.contentLecture.lectureContentId,
//                                                            content.title,
//                                                            content.attempt_id,
//                                                            content.ccid,
//                                                            downloadBtn
//                                                        )
//                                                    } else {
//                                                        showconfirmation(
//                                                            context,
//                                                            "wifi"
//                                                        )
//                                                    }
//                                                } else {
//                                                    startFileDownload(
//                                                        content.contentLecture.lectureId,
//                                                        data.csid,
//                                                        content.contentLecture.lectureContentId,
//                                                        content.title,
//                                                        content.attempt_id,
//                                                        content.ccid,
//                                                        downloadBtn
//                                                    )
//                                                }
//                                            } else {
//                                                MediaUtils.isStoragePermissionGranted(context)
//                                            }
//                                        }
//                                    } else {
//                                        downloadBtn.setOnClickListener {
//
//                                            (context as Activity).alert("This lecture will be deleted !!!") {
//                                                yesButton {
//                                                    val file =
//                                                        context.getExternalFilesDir(Environment.getDataDirectory().absolutePath)
//                                                    val folderFile = File(
//                                                        file,
//                                                        "/${content.contentLecture.lectureId}"
//                                                    )
//                                                    MediaUtils.deleteRecursive(folderFile)
//                                                    viewModel.updateContent(
//                                                        data.csid,
//                                                        content.contentLecture.lectureContentId,
//                                                        "false"
//                                                    )
//                                                }
//                                                noButton { it.dismiss() }
//                                            }.show()
//                                        }
//                                        inflatedView.setOnClickListener {
//                                            if (content.progress == "UNDONE") {
//                                                if (content.progressId.isEmpty())
//                                                    setProgress(
//                                                        content.ccid,
//                                                        content.attempt_id,
//                                                        content.contentable,
//                                                        data.csid,
//                                                        content.contentLecture.lectureContentId
//                                                    )
//                                                else
//                                                    updateProgress(
//                                                        content.ccid,
//                                                        content.attempt_id,
//                                                        content.progressId,
//                                                        "DONE",
//                                                        content.contentable,
//                                                        data.csid,
//                                                        content.contentLecture.lectureContentId
//                                                    )
//                                            }
//                                            it.context.startActivity(
//                                                it.context.intentFor<VideoPlayerActivity>(
//                                                    VIDEO_ID to content.contentLecture.lectureId,
//                                                    RUN_ATTEMPT_ID to content.attempt_id,
//                                                    CONTENT_ID to content.ccid,
//                                                    SECTION_ID to data.csid,
//                                                    DOWNLOADED to true
//                                                ).singleTop()
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                            content.contentable == "document" -> {
//                                contentType.setImageDrawable(context.getDrawable(R.drawable.ic_document))
//                                ll.addView(inflatedView)
//                                if (content.contentDocument.documentContentId.isNotEmpty() && content.contentDocument.documentPdfLink.isNotEmpty()) {
//                                    inflatedView.setOnClickListener {
//                                        if (content.progress == "UNDONE") {
//                                            if (content.progressId.isEmpty())
//                                                setProgress(
//                                                    content.ccid,
//                                                    content.attempt_id,
//                                                    content.contentable,
//                                                    data.csid,
//                                                    content.contentDocument.documentContentId
//                                                )
//                                            else
//                                                updateProgress(
//                                                    content.ccid,
//                                                    content.attempt_id,
//                                                    content.progressId,
//                                                    "DONE",
//                                                    content.contentable,
//                                                    data.csid,
//                                                    content.contentDocument.documentContentId
//                                                )
//                                        }
//                                        it.context.startActivity(
//                                            it.context.intentFor<PdfActivity>(
//                                                "fileUrl" to content.contentDocument.documentPdfLink,
//                                                "fileName" to content.contentDocument.documentName + ".pdf"
//                                            ).singleTop()
//                                        )
//                                    }
//                                }
//                            }
//                            content.contentable == "video" -> {
//                                contentType.setImageDrawable(context.getDrawable(R.drawable.ic_youtube_video))
//                                ll.addView(inflatedView)
//                                if (content.contentVideo.videoContentId.isNotEmpty() && content.contentVideo.videoUrl.isNotEmpty()) {
//                                    inflatedView.setOnClickListener {
//                                        if (content.progress == "UNDONE") {
//                                            if (content.progressId.isEmpty())
//                                                setProgress(
//                                                    content.ccid,
//                                                    content.attempt_id,
//                                                    content.contentable,
//                                                    data.csid,
//                                                    content.contentVideo.videoContentId
//                                                )
//                                            else
//                                                updateProgress(
//                                                    content.ccid,
//                                                    content.attempt_id,
//                                                    content.progressId,
//                                                    "DONE",
//                                                    content.contentable,
//                                                    data.csid,
//                                                    content.contentVideo.videoContentId
//                                                )
//                                        }
//                                        it.context.startActivity(
//                                            it.context.intentFor<VideoPlayerActivity>(
//                                                "videoUrl" to content.contentVideo.videoUrl,
//                                                RUN_ATTEMPT_ID to content.attempt_id,
//                                                CONTENT_ID to content.ccid
//                                            ).singleTop()
//                                        )
//                                    }
//                                }
//                            }
//                            content.contentable == "qna" -> {
//                                contentType.setImageDrawable(context.getDrawable(R.drawable.ic_quiz))
//                                ll.addView(inflatedView)
//                                if (content.contentQna.qnaContentId.isNotEmpty() && content.contentQna.qnaQid.toString().isNotEmpty()) {
//                                    inflatedView.setOnClickListener {
//                                        if (content.progress == "UNDONE") {
//                                            if (content.progressId.isEmpty())
//                                                setProgress(
//                                                    content.ccid,
//                                                    content.attempt_id,
//                                                    content.contentable,
//                                                    data.csid,
//                                                    content.contentQna.qnaContentId
//                                                )
//                                            else
//                                                updateProgress(
//                                                    content.ccid,
//                                                    content.attempt_id,
//                                                    content.progressId,
//                                                    "DONE",
//                                                    content.contentable,
//                                                    data.csid,
//                                                    content.contentQna.qnaContentId
//                                                )
//                                        }
//                                        it.context.startActivity(
//                                            it.context.intentFor<QuizActivity>(
//                                                QUIZ_QNA to content.contentQna.qnaUid,
//                                                RUN_ATTEMPT_ID to content.attempt_id,
//                                                QUIZ_ID to content.contentQna.qnaQid.toString()
//                                            ).singleTop()
//                                        )
//                                    }
//                                }
//                            }
//                            content.contentable == "code_challenge" -> {
//                                contentType.setImageDrawable(context.getDrawable(R.drawable.ic_quiz))
//                                ll.addView(inflatedView)
//                                ll.setOnClickListener {
//                                    showconfirmation(it.context, "unavailable")
//                                }
//                            }
//                        }
//                    } else {
//                        contentType.visibility = View.GONE
//                        downloadBtn.setImageDrawable(context.getDrawable(R.drawable.ic_lock_outline_black_24dp))
//                        ll.addView(inflatedView)
//                    }
//
//                    itemView.setOnClickListener {
//                        if (itemView.title.text.contains("Challenges", true))
//                            showconfirmation(it.context, "unavailable")
//                        else
//                            showOrHide(ll, it)
//                    }
//
//                    itemView.arrow.setOnClickListener {
//                        if (itemView.title.text.contains("Challenges", true))
//                            showconfirmation(it.context, "unavailable")
//                        else
//                            showOrHide(ll, it)
//                    }
//                }
//            }
        }

        private fun startFileDownload(lectureId: String, dataId: String, lectureContentId: String, title: String, attempt_id: String, content_id: String, downloadBtn: ImageView) {
            if (FileUtils.checkIfCannotDownload(context)) {
                FileUtils.showIfCleanDialog(context, object : OnCleanDialogListener {
                    override fun onComplete() {
                        startDownload(
                            lectureId,
                            dataId,
                            lectureContentId,
                            title,
                            attempt_id,
                            content_id,
                            downloadBtn
                        )
                    }
                })
            } else {
                startDownload(
                    lectureId,
                    dataId,
                    lectureContentId,
                    title,
                    attempt_id,
                    content_id,
                    downloadBtn
                )
            }
        }

        private fun startDownload(
            videoId: String,
            id: String,
            lectureContentId: String,
            title: String,
            attemptId: String,
            contentId: String,
            downloadBtn: ImageView
        ) {
            starter.startDownload(
                videoId,
                id,
                lectureContentId,
                title,
                attemptId,
                contentId)
            downloadBtn.isEnabled = false
            (downloadBtn.background as AnimationDrawable).start()
        }
    }


//    fun setProgress(
//        id: String,
//        attempt_id: String,
//        contentable: String,
//        sectionId: String,
//        contentId: String
//    ) {
//        doAsync {
//            val p = Progress()
//            p.status = "DONE"
//            p.runs = RunAttemptsId(attempt_id)
//            p.content = ContentsId(id)
//            Clients.onlineV2JsonApi.setProgress(p).enqueue(retrofitCallback { _, response ->
//
//                response?.body().let {
//                    val progressId = it?.id
//                    when (contentable) {
//                        "lecture" -> thread {
//                            viewModel.updateProgressLecture(
//                                sectionId, contentId, "DONE", progressId
//                                ?: ""
//                            )
//                        }
//
//                        "document" ->
//                            thread {
//                                viewModel.updateProgressDocument(
//                                    sectionId, contentId, "DONE", progressId
//                                    ?: ""
//                                )
//                            }
//                        "video" ->
//                            thread {
//                                viewModel.updateProgressVideo(
//                                    sectionId, contentId, "DONE", progressId
//                                    ?: ""
//                                )
//                            }
//                        "qna" ->
//                            thread {
//                                viewModel.updateProgressQna(
//                                    sectionId, contentId, "DONE", progressId
//                                    ?: ""
//                                )
//                            }
//                        else -> {
//                        }
//                    }
//                }
//            })
//        }
//    }
//
//    private fun updateProgress(
//        id: String,
//        attempt_id: String,
//        progressId: String,
//        status: String,
//        contentable: String,
//        sectionId: String,
//        contentId: String
//    ) {
//        doAsync {
//            val p = Progress()
//            p.id = progressId
//            p.status = status
//            p.runs = RunAttemptsId(attempt_id)
//            p.content = ContentsId(id)
//            Clients.onlineV2JsonApi.updateProgress(progressId, p)
//                .enqueue(retrofitCallback { _, response ->
//                    if (response != null) {
//                        if (response.isSuccessful) {
//                            when (contentable) {
//                                "lecture" -> thread {
//                                    viewModel.updateProgressLecture(
//                                        sectionId,
//                                        contentId,
//                                        status,
//                                        progressId
//                                    )
//                                }
//
//                                "document" ->
//                                    thread {
//                                        viewModel.updateProgressDocument(
//                                            sectionId,
//                                            contentId,
//                                            status,
//                                            progressId
//                                        )
//                                    }
//                                "video" ->
//                                    thread {
//                                        viewModel.updateProgressVideo(
//                                            sectionId,
//                                            contentId,
//                                            status,
//                                            progressId
//                                        )
//                                    }
//                                "qna" ->
//                                    thread {
//                                        viewModel.updateProgressQna(
//                                            sectionId,
//                                            contentId,
//                                            status,
//                                            progressId
//                                        )
//                                    }
//                            }
//                        }
//                    }
//                })
//        }
//    }
}
