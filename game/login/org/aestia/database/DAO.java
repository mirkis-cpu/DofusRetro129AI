// 

// 

package org.aestia.database;

interface DAO<T>
{
    T load(final Object p0);
    
    boolean update(final T p0);
}
