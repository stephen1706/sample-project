package com.stephen.shopback.utils

class PageLinks(linkHeader: String) {
    /**
     * @return first
     */
    var first: String? = null
        private set
    /**
     * @return last
     */
    var last: String? = null
        private set
    /**
     * @return next
     */
    var next: String? = null
        private set
    /**
     * @return prev
     */
    var prev: String? = null
        private set

    init {
        val links = linkHeader.split(DELIM_LINKS.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (link in links) {
            val segments = link.split(DELIM_LINK_PARAM.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (segments.size < 2)
                continue

            var linkPart = segments[0].trim({ it <= ' ' })
            if (!linkPart.startsWith("<") || !linkPart.endsWith(">"))
            //$NON-NLS-1$ //$NON-NLS-2$
                continue
            linkPart = linkPart.substring(1, linkPart.length - 1)

            for (i in 1 until segments.size) {
                val rel = segments[i].trim({ it <= ' ' }).split("=".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray() //$NON-NLS-1$
                if (rel.size < 2 || !META_REL.equals(rel[0]))
                    continue

                var relValue = rel[1]
                if (relValue.startsWith("\"") && relValue.endsWith("\""))
                //$NON-NLS-1$ //$NON-NLS-2$
                    relValue = relValue.substring(1, relValue.length - 1)

                if (META_FIRST.equals(relValue))
                    first = linkPart
                else if (META_LAST.equals(relValue))
                    last = linkPart
                else if (META_NEXT.equals(relValue))
                    next = linkPart
                else if (META_PREV.equals(relValue))
                    prev = linkPart
            }
        }
    }

    companion object {

        /**  */
        val HEADER_LINK = "Link" //$NON-NLS-1$
        /**  */
        val HEADER_NEXT = "X-Next" //$NON-NLS-1$
        /**  */
        val HEADER_LAST = "X-Last" //$NON-NLS-1$

        /**  */
        val META_REL = "rel" //$NON-NLS-1$
        /**  */
        val META_LAST = "last" //$NON-NLS-1$
        /**  */
        val META_NEXT = "next" //$NON-NLS-1$
        /**  */
        val META_FIRST = "first" //$NON-NLS-1$
        /**  */
        val META_PREV = "prev" //$NON-NLS-1$

        private val DELIM_LINKS = "," //$NON-NLS-1$

        private val DELIM_LINK_PARAM = ";" //$NON-NLS-1$
    }
}