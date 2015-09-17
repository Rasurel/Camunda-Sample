# Camunda-Sample
Now I have another problem.
On the "approve the question" form I receive this mistake:

<img src="https://i.gyazo.com/6371da3f19e1b717e92aee2db17843a3.png" >

It writes that QEntity class has no "approved" property. But I did all the same like in the sample "pizza-order", they also don't have such property, but it works. Maybe I don't understand something?

Also, I have next question: 
In the "answer the question" I want to display the question text, but it doesn't work. I managed to display question type, but the text I can't until now.
### How I try to display it in answer.xhtml:
```html
<label for="question">Question Text</label>
          <!-- create process variables using the processVariables map. -->
          <p><textarea name="question" rows="7" cols="50" value="#{processVariables['question']}" readonly="true"></textarea></p>
```
