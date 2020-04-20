package pl.alpheratzteam.database.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/**
 * @author hp888 on 20.04.2020.
 */

@Data
@AllArgsConstructor
public final class Collection
{
    private final String name;
    private final List<String> records;
}