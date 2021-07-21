package kotlin.text;

import java.util.regex.Pattern;
import kotlin.Metadata;

@Metadata(mo6272bv = {1, 0, 3}, mo6273d1 = {"\u0000\f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b¨\u0006\u0003"}, mo6274d2 = {"toRegex", "Lkotlin/text/Regex;", "Ljava/util/regex/Pattern;", "kotlin-stdlib"}, mo6275k = 5, mo6276mv = {1, 1, 15}, mo6278xi = 1, mo6279xs = "kotlin/text/StringsKt")
/* renamed from: kotlin.text.StringsKt__RegexExtensionsJVMKt */
class RegexExtensionsJVM extends Indent {
    private static final Regex toRegex(Pattern pattern) {
        return new Regex(pattern);
    }
}
