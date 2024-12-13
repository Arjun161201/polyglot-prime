# FHIR IG v1.2 CSV Example Directory

This directory contains a valid example dataset that aligns with the [FHIR Implementation Guide (IG) v1.2](https://shinny.org/us/ny/hrsn/index.html). It includes four CSV files that collectively generate the following FHIR resource types:

- **[Bundle](https://shinny.org/us/ny/hrsn/StructureDefinition-SHINNYBundleProfile.html)**
- **[Patient](https://shinny.org/us/ny/hrsn/StructureDefinition-shinny-patient.html)**
- **[Organization](https://shinny.org/us/ny/hrsn/StructureDefinition-shin-ny-organization.html)**
- **[Consent](https://shinny.org/us/ny/hrsn/StructureDefinition-shinny-Consent.html)**
- **[Encounter](https://shinny.org/us/ny/hrsn/StructureDefinition-shinny-encounter.html)**
- **[Observation](https://shinny.org/us/ny/hrsn/StructureDefinition-shinny-observation-screening-response.html)**

## Included CSV Files

1. **`demographic_data.csv`**  
   Contains demographic information necessary for creating Patient resources.
   
2. **`qe_admin_data.csv`**  
   Includes administrative data for organizations.
   
3. **`screening_observation_data.csv`**  
   Provides details used for Observation resources in the screening process.

4. **`screening_profile_data.csv`**  
   Contains data used to create both Encounter and Consent resources and support screening-related processes.

## Mapping CSV Fields to FHIR IG Elements

A detailed mapping of each field in the provided CSV files to their corresponding FHIR IG elements is documented in the [`documentation.auto.md`](https://github.com/tech-by-design/polyglot-prime/blob/main/support/specifications/flat-file/documentation.auto.md) file. This file provides a comprehensive field-by-field mapping and serves as a guide to understanding how the CSV files translate into the FHIR resources.

## Purpose

This example dataset and accompanying documentation demonstrate how to structure flat-file data for transformation into FHIR-compliant resources, aligning with the SHIN-NY guidelines and profiles defined in the IG.
