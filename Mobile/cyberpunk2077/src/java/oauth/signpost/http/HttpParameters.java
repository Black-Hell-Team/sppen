package oauth.signpost.http;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import oauth.signpost.OAuth;

public class HttpParameters implements Map<String, SortedSet<String>>, Serializable {
    private TreeMap<String, SortedSet<String>> wrappedMap = new TreeMap();

    public SortedSet<String> put(String str, SortedSet<String> sortedSet) {
        return (SortedSet) this.wrappedMap.put(str, sortedSet);
    }

    public SortedSet<String> put(String str, SortedSet<String> sortedSet, boolean z) {
        if (!z) {
            return (SortedSet) this.wrappedMap.put(str, sortedSet);
        }
        remove((Object) str);
        for (String put : sortedSet) {
            put(str, put, true);
        }
        return get((Object) str);
    }

    public String put(String str, String str2) {
        return put(str, str2, false);
    }

    public String put(String str, String str2, boolean z) {
        Object str3;
        if (z) {
            str3 = OAuth.percentEncode(str3);
        }
        SortedSet sortedSet = (SortedSet) this.wrappedMap.get(str3);
        if (sortedSet == null) {
            sortedSet = new TreeSet();
            this.wrappedMap.put(str3, sortedSet);
        }
        if (str2 != null) {
            if (z) {
                str2 = OAuth.percentEncode(str2);
            }
            sortedSet.add(str2);
        }
        return str2;
    }

    public String putNull(String str, String str2) {
        return put(str, str2);
    }

    public void putAll(Map<? extends String, ? extends SortedSet<String>> map) {
        this.wrappedMap.putAll(map);
    }

    public void putAll(Map<? extends String, ? extends SortedSet<String>> map, boolean z) {
        if (z) {
            for (String str : map.keySet()) {
                put(str, (SortedSet) map.get(str), true);
            }
            return;
        }
        this.wrappedMap.putAll(map);
    }

    public void putAll(String[] strArr, boolean z) {
        for (int i = 0; i < strArr.length - 1; i += 2) {
            put(strArr[i], strArr[i + 1], z);
        }
    }

    public void putMap(Map<String, List<String>> map) {
        for (String str : map.keySet()) {
            SortedSet sortedSet = get((Object) str);
            if (sortedSet == null) {
                sortedSet = new TreeSet();
                put(str, sortedSet);
            }
            sortedSet.addAll((Collection) map.get(str));
        }
    }

    public SortedSet<String> get(Object obj) {
        return (SortedSet) this.wrappedMap.get(obj);
    }

    public String getFirst(Object obj) {
        return getFirst(obj, false);
    }

    public String getFirst(Object obj, boolean z) {
        SortedSet sortedSet = (SortedSet) this.wrappedMap.get(obj);
        if (sortedSet == null || sortedSet.isEmpty()) {
            return null;
        }
        String str = (String) sortedSet.first();
        if (z) {
            str = OAuth.percentDecode(str);
        }
        return str;
    }

    public String getAsQueryString(Object obj) {
        return getAsQueryString(obj, true);
    }

    public String getAsQueryString(Object obj, boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        if (z) {
            obj = OAuth.percentEncode((String) obj);
        }
        Set set = (Set) this.wrappedMap.get(obj);
        String str = "=";
        if (set == null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(obj);
            stringBuilder2.append(str);
            return stringBuilder2.toString();
        }
        Iterator it = set.iterator();
        while (it.hasNext()) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(obj);
            stringBuilder3.append(str);
            stringBuilder3.append((String) it.next());
            stringBuilder.append(stringBuilder3.toString());
            if (it.hasNext()) {
                stringBuilder.append("&");
            }
        }
        return stringBuilder.toString();
    }

    public String getAsHeaderElement(String str) {
        String first = getFirst(str);
        if (first == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("=\"");
        stringBuilder.append(first);
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }

    public boolean containsKey(Object obj) {
        return this.wrappedMap.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        for (SortedSet contains : this.wrappedMap.values()) {
            if (contains.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        int i = 0;
        for (String str : this.wrappedMap.keySet()) {
            i += ((SortedSet) this.wrappedMap.get(str)).size();
        }
        return i;
    }

    public boolean isEmpty() {
        return this.wrappedMap.isEmpty();
    }

    public void clear() {
        this.wrappedMap.clear();
    }

    public SortedSet<String> remove(Object obj) {
        return (SortedSet) this.wrappedMap.remove(obj);
    }

    public Set<String> keySet() {
        return this.wrappedMap.keySet();
    }

    public Collection<SortedSet<String>> values() {
        return this.wrappedMap.values();
    }

    public Set<Entry<String, SortedSet<String>>> entrySet() {
        return this.wrappedMap.entrySet();
    }

    public HttpParameters getOAuthParameters() {
        HttpParameters httpParameters = new HttpParameters();
        for (Entry entry : entrySet()) {
            String str = (String) entry.getKey();
            if (str.startsWith("oauth_") || str.startsWith("x_oauth_")) {
                httpParameters.put(str, (SortedSet) entry.getValue());
            }
        }
        return httpParameters;
    }
}
