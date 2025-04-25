# `backend-core` Configuration

This document provides an overview of the configuration properties available in the `application.yaml` file. These
properties control various functionalities such as caching, cryptography, masking, and key utilities.

---

## Configuration Overview

### 1. Caching

Controls the caching feature of the application.

| Property                       | Type    | Default Value | Description                  |
|--------------------------------|---------|---------------|------------------------------|
| `backend-core.caching.enabled` | Boolean | `false`       | Enables or disables caching. |

---

### 2. Cryptography

Defines cryptographic algorithms for RSA and AES encryption.

| Property                             | Type    | Default Value          | Description                                 |
|--------------------------------------|---------|------------------------|---------------------------------------------|
| `backend-core.crypto.enabled`        | Boolean | `false`                | Enables or disables cryptographic features. |
| `backend-core.crypto.rsa.algorithms` | String  | `RSA/ECB/PKCS1Padding` | RSA algorithm to be used.                   |
| `backend-core.crypto.aes.algorithms` | String  | `AES/CBC/PKCS5Padding` | AES algorithm to be used.                   |

**Notes:**

- Default algorithms can be overridden using environment variables:
    - `RSA_ALGORITHMS`
    - `AES_ALGORITHMS`

---

### 3. Masking

Controls data masking functionality, typically used for sensitive information.

| Property                                     | Type         | Default Value | Description                       |
|----------------------------------------------|--------------|---------------|-----------------------------------|
| `backend-core.mask.enabled`                  | Boolean      | `false`       | Enables or disables data masking. |
| `backend-core.mask.hidden-types.type.fileds` | List<String> | `[]`          | Fields where to hide.             |

---

### 4. Key Utility

Specifies utility settings for key generation.

| Property                                 | Type   | Default Value | Description                          |
|------------------------------------------|--------|---------------|--------------------------------------|
| `backend-core.key-util.date-format-seed` | String | `dd-MM-yyyy`  | Date format used for key generation. |

---

## Environment Variables

To override default values, use the following environment variables:

| Environment Variable | Description             |
|----------------------|-------------------------|
| `RSA_ALGORITHMS`     | Sets the RSA algorithm. |
| `AES_ALGORITHMS`     | Sets the AES algorithm. |

---

## Usage Example

```yaml
backend-core:
    caching:
        enabled: true
        
    crypto:
        enabled: true
        rsa:
            algorithms: RSA/ECB/PKCS1Padding
        aes:
            algorithms: AES/GCM/NoPadding

    mask:
        enabled: true
        hidden-types:
            all:
                fields: example,example
                
            head:
                fields: phone,mobileNumber
              
            middle:
                fields: cardNumber,creditCardNumber
      
            tail:
                fields: nationalId,username
                
            two-sides:
                fields: example,example

    key-util:
        date-format-seed: yyyy-MM-dd
