package com.netflix.mce.schemavalidator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ValidateSchemaSpec extends Specification {

    private static File YML_DIRECTORY = new File("configs")
    private static File SCHEMA_FILE = new File("schema/schema.json")

    @Shared
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory())

    @Shared
    JsonSchemaFactory factory = JsonSchemaFactory.builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)).objectMapper(mapper).build()

    @Unroll("Validating yaml schema for #file")
    void "validate all items in configs directory against schema"(){
        given:
        Set invalidMessages = factory.getSchema(SCHEMA_FILE.text).validate(mapper.readTree(file.text)).message

        if(!invalidMessages.empty){
            println "Schema validation failed: ${file.name}"
            invalidMessages.each{ println it }
        }

        expect:
        invalidMessages.isEmpty()

        where:
        file << YML_DIRECTORY.listFiles()
    }

}
