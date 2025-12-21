package vn.tutorial.cinemate.presentation.streaming

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

/**
 * EventEmitter base class for event handling
 */
abstract class EventEmitter<T : Any> {
    
    private val listeners = ConcurrentHashMap<String, MutableList<(Any) -> Unit>>()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    fun on(event: String, listener: (Any) -> Unit) {
        listeners.getOrPut(event) { mutableListOf() }.add(listener)
    }
    
    fun off(event: String, listener: ((Any) -> Unit)? = null) {
        if (listener == null) {
            listeners.remove(event)
        } else {
            listeners[event]?.remove(listener)
        }
    }
    
    protected fun emit(event: String, vararg args: Any?) {
        listeners[event]?.forEach { listener ->
            scope.launch {
                try {
                    listener(args.firstOrNull() ?: Unit)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    fun destroy() {
        listeners.clear()
        scope.cancel()
    }
}

/**
 * Simple event types for managers
 */
interface MseManagerEvents
interface BufferManagerEvents
interface AbrManagerEvents
interface SignalingClientEvents
interface PeerManagerEvents
