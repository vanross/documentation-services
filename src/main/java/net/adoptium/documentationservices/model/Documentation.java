package net.adoptium.documentationservices.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A documentation that can have documents in multiple languages.
 */
public class Documentation {

    private final String id;

    private final Set<Document> documents;

    public Documentation(final String id, final Collection<Document> documents) {
        this.id = Objects.requireNonNull(id);
        if(this.id.isBlank()) {
            throw new IllegalArgumentException("ID of document must not be blank");
        }
        this.documents = Collections.unmodifiableSet(new HashSet<>(documents));

        //Check if at least english is present
        getEnglishDocument();
    }

    public String getId() {
        return id;
    }

    public Stream<Document> getDocuments() {
        return documents.stream();
    }

    public Document getDocument(final Locale locale) {
        return Optional.ofNullable(locale)
                .map(l -> getDocuments().filter(d -> Objects.equals(l, d.getLocale())).findAny().orElse(null))
                .orElse(getEnglishDocument());
    }

    private Document getEnglishDocument() {
        return getDocuments().filter(d -> Objects.equals(Locale.ENGLISH, d.getLocale()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No english document provided for ID=" + id));
    }
}
