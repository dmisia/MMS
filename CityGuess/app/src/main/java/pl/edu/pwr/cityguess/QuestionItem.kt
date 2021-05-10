package pl.edu.pwr.cityguess


/**
 * Created by stevyhacker on 23.7.14..
 */
class QuestionItem {
    var id = 0
    var level = 0
    var question: String? = null
    var answer: String? = null
    var option1: String? = null
    var option2: String? = null
    var option3: String? = null
    var imageName: String? = null

    constructor() {}
    constructor(id: Int, question: String?, level: Int, answer: String?, option1: String?, option2: String?, option3: String?, imageName: String?) {
        this.id = id
        this.question = question
        this.level = level
        this.answer = answer
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.imageName = imageName
    }

}