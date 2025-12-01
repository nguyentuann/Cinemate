package vn.tutorial.cinemate.presentation.detail.connect.webrtc

import android.content.Context
import org.webrtc.DataChannel
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import java.nio.ByteBuffer

//class WebRtcManager(private val context: Context) {
//
//    private val eglBase = EglBase.create()
//    private val factory: PeerConnectionFactory
//
//    init {
//        PeerConnectionFactory.initialize(
//            PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions()
//        )
//        factory = PeerConnectionFactory.builder().createPeerConnectionFactory()
//    }
//
//    fun createPeerConnection(
//        iceServers: List<PeerConnection.IceServer>,
//        onDataChannelMessage: (ByteArray) -> Unit
//    ): PeerConnection? {
//        val config = PeerConnection.RTCConfiguration(iceServers)
//        config.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
//
//        return factory.createPeerConnection(config, object : PeerConnection.Observer {
//            override fun onDataChannel(dc: DataChannel?) {
//                dc?.registerObserver(object : DataChannel.Observer {
//                    override fun onBufferedAmountChange(p0: Long) {}
//                    override fun onStateChange() {}
//                    override fun onMessage(buffer: DataChannel.Buffer) {
//                        val bytes = ByteArray(buffer.data.remaining())
//                        buffer.data.get(bytes)
//                        onDataChannelMessage(bytes)
//                    }
//                })
//            }
//            override fun onIceCandidate(candidate: IceCandidate?) {
//                candidate?.let {
//                    // gửi ICE candidate tới peer khác qua WebSocket
//                }
//            }
//            // Các callback còn lại để trống
//            override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
//            override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
//            override fun onIceConnectionReceivingChange(p0: Boolean) {}
//            override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
//            override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
//            override fun onAddStream(p0: MediaStream?) {}
//            override fun onRemoveStream(p0: MediaStream?) {}
//            override fun onRenegotiationNeeded() {}
//            override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {}
//        })
//    }
//
//    fun createDataChannel(peerConnection: PeerConnection, label: String = "segmentChannel"): DataChannel {
//        val init = DataChannel.Init()
//        init.ordered = true
//        return peerConnection.createDataChannel(label, init)
//    }
//
//    fun sendSegment(dc: DataChannel, segment: ByteArray) {
//        val buffer = DataChannel.Buffer(ByteBuffer.wrap(segment), false)
//        dc.send(buffer)
//    }
//}
