const mongoose = require('mongoose');
const Schema = mongoose.Schema;

//SECTION  collection and schema for Registration
let ClassroomSchema = new Schema({
    classroom_name: {
        type: String,
        unique:true,
        match: [/^[a-zA-Z0-9-_]+$/, 'is invalid'],
        required: [true, "can't be blank"],

    },
    current_light_configuration: {
        type: String,
        match: [/^[0-1]{19}$/, 'is invalid'],
        required: [true, "can't be blank"]
    },
    description: {
        type: String,
        required: true,
        trim: true
    }
}, {
    collection: 'Classroom'
});

module.exports = mongoose.model('Classroom',ClassroomSchema);