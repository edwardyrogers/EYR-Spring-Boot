package com.eyr.demo.core.models

import org.springframework.data.domain.Page

class BizLogicModel {

    /**
     * A sealed class representing the request model, which can either be a given type of returning data of a Void of no return.
     */
    sealed class REQ {

        /**
         * Represents a generic request for fetching data.
         *
         * @param T The type of data being requested.
         * @property projection The class type of the data to be fetched, used to indicate the data model.
         */
        open class Data<T>(
            open val projection: Class<T>
        )

        /**
         * Represents a void return.
         */
        open class Void

        /**
         * Represents a common value return.
         */
        open class ValueReturn
    }

    /**
     * A sealed class representing the result of a fetch operation, which can either be a single item of type T or a paginated result of type Page<T>.
     */
    open class RES {
        /**
         * Represents a result containing a single data item of type T.
         */
        open class Single<T>(
            open val data: T
        )

        /**
         * Represents a result containing a paginated list of items of type T.
         */
        open class Paginated<T>(
            open val data: Page<T>
        )
    }
}