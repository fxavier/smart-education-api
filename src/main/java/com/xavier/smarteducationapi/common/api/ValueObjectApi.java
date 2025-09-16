package com.xavier.smarteducationapi.common.api;

import com.xavier.smarteducationapi.common.domain.valueobject.Address;
import com.xavier.smarteducationapi.common.domain.valueobject.Email;
import com.xavier.smarteducationapi.common.domain.valueobject.Money;
import com.xavier.smarteducationapi.common.domain.valueobject.Phone;
import com.xavier.smarteducationapi.common.domain.valueobject.TenantId;

/**
 * API facade for value object operations.
 * 
 * This interface provides access to common value objects that can be
 * used across modules, such as Address, Email, Money, etc.
 * 
 * @author Xavier Nhagumbe
 * @version 1.0
 * @since 2025-09-15
 */
public interface ValueObjectApi {
    
    /**
     * Gets the Address value object class.
     * 
     * @return the Address class
     */
    Class<Address> getAddressClass();
    
    /**
     * Gets the Email value object class.
     * 
     * @return the Email class
     */
    Class<Email> getEmailClass();
    
    /**
     * Gets the Money value object class.
     * 
     * @return the Money class
     */
    Class<Money> getMoneyClass();
    
    /**
     * Gets the Phone value object class.
     * 
     * @return the Phone class
     */
    Class<Phone> getPhoneClass();
    
    /**
     * Gets the TenantId value object class.
     * 
     * @return the TenantId class
     */
    Class<TenantId> getTenantIdClass();
    
}
