package kotlin.jvm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.MustBeDocumented;

@Target({ElementType.FIELD})
@kotlin.annotation.Target(allowedTargets = {AnnotationTarget.FIELD})
@Retention(RetentionPolicy.SOURCE)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo6274d2 = {"Lkotlin/jvm/Volatile;", "", "kotlin-stdlib"}, mo6275k = 1, mo6276mv = {1, 1, 15})
@Documented
/* compiled from: JvmFlagAnnotations.kt */
public @interface Volatile {
}
