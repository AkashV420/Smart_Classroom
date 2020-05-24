const mongoose = require('mongoose');
const Schema = mongoose.Schema;

//SECTION  collection and schema for Registration
let LightSchema = new Schema({
    layout_name: {
        type: String,
        unique:true,
        match: [/^[a-zA-Z0-9_]+$/, 'is invalid'],
        required: [true, "can't be blank"],

    },
    light_configuration: {
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
    collection: 'Light_layout'
});

module.exports = mongoose.model('Light_layout', LightSchema);