package org.reactivestreams;

import java.util.Objects;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public final class FlowAdapters {

    static final class FlowPublisherFromReactive<T> implements Publisher<T> {
        final Publisher<? extends T> reactiveStreams;

        public FlowPublisherFromReactive(Publisher<? extends T> publisher) {
            this.reactiveStreams = publisher;
        }

        public void subscribe(Subscriber<? super T> subscriber) {
            this.reactiveStreams.subscribe(subscriber == null ? null : new ReactiveToFlowSubscriber(subscriber));
        }
    }

    static final class FlowToReactiveProcessor<T, U> implements Processor<T, U> {
        final Processor<? super T, ? extends U> reactiveStreams;

        public FlowToReactiveProcessor(Processor<? super T, ? extends U> processor) {
            this.reactiveStreams = processor;
        }

        public void onSubscribe(Subscription subscription) {
            this.reactiveStreams.onSubscribe(subscription == null ? null : new ReactiveToFlowSubscription(subscription));
        }

        public void onNext(T t) {
            this.reactiveStreams.onNext(t);
        }

        public void onError(Throwable th) {
            this.reactiveStreams.onError(th);
        }

        public void onComplete() {
            this.reactiveStreams.onComplete();
        }

        public void subscribe(Subscriber<? super U> subscriber) {
            this.reactiveStreams.subscribe(subscriber == null ? null : new ReactiveToFlowSubscriber(subscriber));
        }
    }

    static final class FlowToReactiveSubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> reactiveStreams;

        public FlowToReactiveSubscriber(Subscriber<? super T> subscriber) {
            this.reactiveStreams = subscriber;
        }

        public void onSubscribe(Subscription subscription) {
            this.reactiveStreams.onSubscribe(subscription == null ? null : new ReactiveToFlowSubscription(subscription));
        }

        public void onNext(T t) {
            this.reactiveStreams.onNext(t);
        }

        public void onError(Throwable th) {
            this.reactiveStreams.onError(th);
        }

        public void onComplete() {
            this.reactiveStreams.onComplete();
        }
    }

    static final class FlowToReactiveSubscription implements Subscription {
        final Subscription reactiveStreams;

        public FlowToReactiveSubscription(Subscription subscription) {
            this.reactiveStreams = subscription;
        }

        public void request(long j) {
            this.reactiveStreams.request(j);
        }

        public void cancel() {
            this.reactiveStreams.cancel();
        }
    }

    static final class ReactivePublisherFromFlow<T> implements Publisher<T> {
        final Publisher<? extends T> flow;

        public ReactivePublisherFromFlow(Publisher<? extends T> publisher) {
            this.flow = publisher;
        }

        public void subscribe(Subscriber<? super T> subscriber) {
            this.flow.subscribe(subscriber == null ? null : new FlowToReactiveSubscriber(subscriber));
        }
    }

    static final class ReactiveToFlowProcessor<T, U> implements Processor<T, U> {
        final Processor<? super T, ? extends U> flow;

        public ReactiveToFlowProcessor(Processor<? super T, ? extends U> processor) {
            this.flow = processor;
        }

        public void onSubscribe(Subscription subscription) {
            this.flow.onSubscribe(subscription == null ? null : new FlowToReactiveSubscription(subscription));
        }

        public void onNext(T t) {
            this.flow.onNext(t);
        }

        public void onError(Throwable th) {
            this.flow.onError(th);
        }

        public void onComplete() {
            this.flow.onComplete();
        }

        public void subscribe(Subscriber<? super U> subscriber) {
            this.flow.subscribe(subscriber == null ? null : new FlowToReactiveSubscriber(subscriber));
        }
    }

    static final class ReactiveToFlowSubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> flow;

        public ReactiveToFlowSubscriber(Subscriber<? super T> subscriber) {
            this.flow = subscriber;
        }

        public void onSubscribe(Subscription subscription) {
            this.flow.onSubscribe(subscription == null ? null : new FlowToReactiveSubscription(subscription));
        }

        public void onNext(T t) {
            this.flow.onNext(t);
        }

        public void onError(Throwable th) {
            this.flow.onError(th);
        }

        public void onComplete() {
            this.flow.onComplete();
        }
    }

    static final class ReactiveToFlowSubscription implements Subscription {
        final Subscription flow;

        public ReactiveToFlowSubscription(Subscription subscription) {
            this.flow = subscription;
        }

        public void request(long j) {
            this.flow.request(j);
        }

        public void cancel() {
            this.flow.cancel();
        }
    }

    private FlowAdapters() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Publisher<T> toPublisher(Publisher<? extends T> publisher) {
        Objects.requireNonNull(publisher, "flowPublisher");
        if (publisher instanceof FlowPublisherFromReactive) {
            return ((FlowPublisherFromReactive) publisher).reactiveStreams;
        }
        if (publisher instanceof Publisher) {
            return (Publisher) publisher;
        }
        return new ReactivePublisherFromFlow(publisher);
    }

    public static <T> Publisher<T> toFlowPublisher(Publisher<? extends T> publisher) {
        Objects.requireNonNull(publisher, "reactiveStreamsPublisher");
        if (publisher instanceof ReactivePublisherFromFlow) {
            return ((ReactivePublisherFromFlow) publisher).flow;
        }
        if (publisher instanceof Publisher) {
            return (Publisher) publisher;
        }
        return new FlowPublisherFromReactive(publisher);
    }

    public static <T, U> Processor<T, U> toProcessor(Processor<? super T, ? extends U> processor) {
        Objects.requireNonNull(processor, "flowProcessor");
        if (processor instanceof FlowToReactiveProcessor) {
            return ((FlowToReactiveProcessor) processor).reactiveStreams;
        }
        if (processor instanceof Processor) {
            return (Processor) processor;
        }
        return new ReactiveToFlowProcessor(processor);
    }

    public static <T, U> Processor<T, U> toFlowProcessor(Processor<? super T, ? extends U> processor) {
        Objects.requireNonNull(processor, "reactiveStreamsProcessor");
        if (processor instanceof ReactiveToFlowProcessor) {
            return ((ReactiveToFlowProcessor) processor).flow;
        }
        if (processor instanceof Processor) {
            return (Processor) processor;
        }
        return new FlowToReactiveProcessor(processor);
    }

    public static <T> Subscriber<T> toFlowSubscriber(Subscriber<T> subscriber) {
        Objects.requireNonNull(subscriber, "reactiveStreamsSubscriber");
        if (subscriber instanceof ReactiveToFlowSubscriber) {
            return ((ReactiveToFlowSubscriber) subscriber).flow;
        }
        if (subscriber instanceof Subscriber) {
            return (Subscriber) subscriber;
        }
        return new FlowToReactiveSubscriber(subscriber);
    }

    public static <T> Subscriber<T> toSubscriber(Subscriber<T> subscriber) {
        Objects.requireNonNull(subscriber, "flowSubscriber");
        if (subscriber instanceof FlowToReactiveSubscriber) {
            return ((FlowToReactiveSubscriber) subscriber).reactiveStreams;
        }
        if (subscriber instanceof Subscriber) {
            return (Subscriber) subscriber;
        }
        return new ReactiveToFlowSubscriber(subscriber);
    }
}
