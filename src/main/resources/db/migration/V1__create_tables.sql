-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Center table (main entity)
CREATE TABLE center (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Basic info
    kinder_code VARCHAR(50) NOT NULL UNIQUE,
    offic_edu VARCHAR(100),
    sub_office_edu VARCHAR(100),
    name VARCHAR(200) NOT NULL,
    establish_type VARCHAR(50),
    representative_name VARCHAR(100),
    director_name VARCHAR(100),
    establish_date VARCHAR(50),
    open_date VARCHAR(50),
    address VARCHAR(500),
    phone VARCHAR(50),
    fax VARCHAR(50),
    homepage VARCHAR(500),
    operating_hours VARCHAR(200),

    -- Class counts
    class_count_3 INTEGER,
    class_count_4 INTEGER,
    class_count_5 INTEGER,
    mixed_class_count INTEGER,
    special_class_count INTEGER,

    -- Capacity
    total_capacity INTEGER,
    capacity_3 INTEGER,
    capacity_4 INTEGER,
    capacity_5 INTEGER,
    mixed_capacity INTEGER,
    special_capacity INTEGER,

    -- Enrollment
    enrollment_3 INTEGER,
    enrollment_4 INTEGER,
    enrollment_5 INTEGER,
    mixed_enrollment INTEGER,
    special_enrollment INTEGER,

    -- Location (PostGIS)
    location GEOGRAPHY(Point, 4326),

    -- Meta
    disclosure_timing VARCHAR(50),
    acting_director VARCHAR(50),
    source_updated_at TIMESTAMP
);

-- Indexes for center table
CREATE INDEX idx_center_kinder_code ON center(kinder_code);
CREATE INDEX idx_center_establish_type ON center(establish_type);
CREATE INDEX idx_center_name ON center(name);
CREATE INDEX idx_center_location ON center USING GIST(location);

-- Building information
CREATE TABLE center_building (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    arch_year VARCHAR(50),
    floor_count INTEGER,
    building_area DOUBLE PRECISION,
    total_land_area DOUBLE PRECISION,
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_building_center_id ON center_building(center_id);

-- Classroom information
CREATE TABLE center_classroom (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    classroom_count INTEGER,
    classroom_area DOUBLE PRECISION,
    playground_area DOUBLE PRECISION,
    health_area DOUBLE PRECISION,
    kitchen_area DOUBLE PRECISION,
    other_area DOUBLE PRECISION,
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_classroom_center_id ON center_classroom(center_id);

-- Teacher information
CREATE TABLE center_teacher (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    -- Teacher counts
    director_count INTEGER,
    vice_director_count INTEGER,
    master_teacher_count INTEGER,
    lead_teacher_count INTEGER,
    general_teacher_count INTEGER,
    special_teacher_count INTEGER,
    health_teacher_count INTEGER,
    nutrition_teacher_count INTEGER,
    contract_teacher_count INTEGER,
    staff_count INTEGER,

    -- Qualifications
    master_qual_count INTEGER,
    grade1_qual_count INTEGER,
    grade2_qual_count INTEGER,
    assistant_qual_count INTEGER,
    special_school_qual_count INTEGER,
    health_qual_count INTEGER,
    nutrition_qual_count INTEGER,

    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_teacher_center_id ON center_teacher(center_id);

-- Lesson day information
CREATE TABLE center_lesson_day (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    lesson_days_3 INTEGER,
    lesson_days_4 INTEGER,
    lesson_days_5 INTEGER,
    mixed_lesson_days INTEGER,
    special_lesson_days INTEGER,
    after_school_lesson_days INTEGER,
    below_legal_days VARCHAR(50),
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_lesson_day_center_id ON center_lesson_day(center_id);

-- Meal information
CREATE TABLE center_meal (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    meal_operation_type VARCHAR(100),
    consignment_company VARCHAR(200),
    total_children INTEGER,
    meal_children INTEGER,
    nutrition_teacher_assigned VARCHAR(50),
    single_nutrition_teacher_count INTEGER,
    joint_nutrition_teacher_count INTEGER,
    joint_institution_name VARCHAR(200),
    cook_count INTEGER,
    cooking_staff_count INTEGER,
    mass_kitchen_registered VARCHAR(50),
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_meal_center_id ON center_meal(center_id);

-- Bus information
CREATE TABLE center_bus (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    bus_operating VARCHAR(50),
    operating_bus_count INTEGER,
    registered_bus_count INTEGER,
    bus_9_seat INTEGER,
    bus_12_seat INTEGER,
    bus_15_seat INTEGER,
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_bus_center_id ON center_bus(center_id);

-- Year of work information
CREATE TABLE center_year_of_work (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    under_1_year INTEGER,
    between_1_and_2_years INTEGER,
    between_2_and_4_years INTEGER,
    between_4_and_6_years INTEGER,
    over_6_years INTEGER,
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_year_of_work_center_id ON center_year_of_work(center_id);

-- Environment information
CREATE TABLE center_environment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    air_quality_check_date VARCHAR(50),
    air_quality_check_result VARCHAR(50),
    regular_disinfection_required VARCHAR(50),
    regular_disinfection_date VARCHAR(50),
    regular_disinfection_result VARCHAR(50),
    water_type_01 VARCHAR(50),
    water_type_02 VARCHAR(50),
    water_type_03 VARCHAR(50),
    water_type_04 VARCHAR(50),
    groundwater_test_required VARCHAR(50),
    groundwater_test_date VARCHAR(50),
    groundwater_test_result VARCHAR(50),
    dust_check_date VARCHAR(50),
    dust_check_result VARCHAR(50),
    light_check_date VARCHAR(50),
    light_check_result VARCHAR(50),
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_environment_center_id ON center_environment(center_id);

-- Safety check information
CREATE TABLE center_safety_check (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    fire_evacuation_yn VARCHAR(10),
    fire_evacuation_date VARCHAR(50),
    gas_check_yn VARCHAR(10),
    gas_check_date VARCHAR(50),
    fire_safety_yn VARCHAR(10),
    fire_safety_date VARCHAR(50),
    electric_check_yn VARCHAR(10),
    electric_check_date VARCHAR(50),
    playground_check_yn VARCHAR(10),
    playground_check_date VARCHAR(50),
    playground_check_result VARCHAR(50),
    cctv_installed VARCHAR(10),
    cctv_total INTEGER,
    cctv_indoor INTEGER,
    cctv_outdoor INTEGER,
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_safety_check_center_id ON center_safety_check(center_id);

-- Safety education information (1:N - semester-based)
CREATE TABLE center_safety_education (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    semester VARCHAR(50),
    life_safety VARCHAR(50),
    traffic_safety VARCHAR(50),
    violence_prevention VARCHAR(50),
    drug_prevention VARCHAR(50),
    cyber_prevention VARCHAR(50),
    disaster_safety VARCHAR(50),
    occupational_safety VARCHAR(50),
    first_aid VARCHAR(50),
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_safety_education_center_id ON center_safety_education(center_id);

-- Mutual aid information
CREATE TABLE center_mutual_aid (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    school_safety_target VARCHAR(50),
    school_safety_enrolled VARCHAR(50),
    education_facility_target VARCHAR(50),
    education_facility_enrolled VARCHAR(50),
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_mutual_aid_center_id ON center_mutual_aid(center_id);

-- Insurance information (1:N - per insurance)
CREATE TABLE center_insurance (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    insurance_name VARCHAR(200),
    target_yn VARCHAR(10),
    enrolled_yn VARCHAR(10),
    company_1 VARCHAR(200),
    company_2 VARCHAR(200),
    company_3 VARCHAR(200),
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_insurance_center_id ON center_insurance(center_id);

-- After school information
CREATE TABLE center_after_school (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    independent_class_count INTEGER,
    afternoon_class_count INTEGER,
    operating_hours VARCHAR(200),
    independent_participants INTEGER,
    afternoon_participants INTEGER,
    regular_teacher_count INTEGER,
    contract_teacher_count INTEGER,
    dedicated_staff_count INTEGER,
    disclosure_timing VARCHAR(50)
);

CREATE INDEX idx_center_after_school_center_id ON center_after_school(center_id);

-- Favorite table
CREATE TABLE favorite (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    device_id VARCHAR(255) NOT NULL,
    center_id UUID NOT NULL REFERENCES center(id) ON DELETE CASCADE,

    UNIQUE(device_id, center_id)
);

CREATE INDEX idx_favorite_device_id ON favorite(device_id);
CREATE INDEX idx_favorite_center_id ON favorite(center_id);
