package kotlin.p004io;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* access modifiers changed from: package-private */
@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007"}, mo6274d2 = {"walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib"}, mo6275k = 5, mo6276mv = {1, 1, 15}, mo6278xi = 1, mo6279xs = "kotlin/io/FilesKt")
/* renamed from: kotlin.io.FilesKt__FileTreeWalkKt */
/* compiled from: FileTreeWalk.kt */
public class FilesKt__FileTreeWalkKt extends FileReadWrite {
    public static /* synthetic */ FileTreeWalk walk$default(File file, FileWalkDirection fileWalkDirection, int i, Object obj) {
        if ((i & 1) != 0) {
            fileWalkDirection = FileWalkDirection.TOP_DOWN;
        }
        return FilesKt.walk(file, fileWalkDirection);
    }

    public static final FileTreeWalk walk(File file, FileWalkDirection fileWalkDirection) {
        Intrinsics.checkParameterIsNotNull(file, "$this$walk");
        Intrinsics.checkParameterIsNotNull(fileWalkDirection, "direction");
        return new FileTreeWalk(file, fileWalkDirection);
    }

    public static final FileTreeWalk walkTopDown(File file) {
        Intrinsics.checkParameterIsNotNull(file, "$this$walkTopDown");
        return FilesKt.walk(file, FileWalkDirection.TOP_DOWN);
    }

    public static final FileTreeWalk walkBottomUp(File file) {
        Intrinsics.checkParameterIsNotNull(file, "$this$walkBottomUp");
        return FilesKt.walk(file, FileWalkDirection.BOTTOM_UP);
    }
}
