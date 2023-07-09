/*const testForm = {
  formId: 1,
  formName: "TestForm",
  description: null,
  fields: [
    {
      title: "First Name",
      description: "The name that you were given at birth.",
      type: "TEXT_FIELD",
    },
    { title: "Vote", type: "MULTIPLE_CHOICE", values: ["Ahmad", "Saud"] },
    {
      title: "Vehicle",
      description: "Choose Your Vehicle",
      type: "CHECKBOXES",
      values: ["Car", "Bike"],
    },
  ],
};*/

function createQuestionCard() {
  let a = document.createElement("div");
  a.classList.add("question-card");
  return a;
}

function createTitleCard(form) {
  let title = document.createElement("h1");
  title.textContent = form.formName;
  let description = document.createElement("p");
  description.textContent = form.description ? form.description : "";
  let titleCard = createQuestionCard();
  titleCard.appendChild(title);
  titleCard.appendChild(description);
  return titleCard;
}

function createField(field) {
  let card = createQuestionCard();
  let fieldTitle = document.createElement("h2");
  fieldTitle.textContent = field.title;
  card.appendChild(fieldTitle);

  let fieldDescription = document.createElement("p");
  fieldDescription.textContent = field.description;
  card.appendChild(fieldDescription);

  if (field.type != "TEXT_FIELD") {
    for (let i in field.values) {
      let inputField = document.createElement("input");
      inputField.setAttribute(
        "type",
        field.type === "CHECKBOXES" ? "checkbox" : "radio"
      );
      inputField.setAttribute(
        "name",
        field.type === "CHECKBOXES" ? field.values[i] : field.title
      );
      inputField.setAttribute(
        "value",
        field.type === "CHECKBOXES" ? "true" : field.values[i]
      );
      inputField.setAttribute("id", field.values[i]);
      card.appendChild(inputField);

      let label = document.createElement("label");
      label.setAttribute("for", field.values[i]);
      label.textContent = field.values[i];
      card.appendChild(label);
      card.appendChild(document.createElement("br"));
    }
  } else {
    let inputField = document.createElement("input");
    inputField.setAttribute("type", "text");
    inputField.setAttribute("name", field.title);
    inputField.classList.add("textinput");
    card.appendChild(inputField);
  }

  return card;
}

function renderForm(form, addButton) {
  if (!form) return;

  let formElement = document.createElement("form");
  formElement.appendChild(createTitleCard(form));

  formElement.setAttribute("method", "POST");
  if (form.formId)
    formElement.setAttribute("action", "/form/" + formId + "/submit");

  let fields = form.fields;
  for (let i in fields) formElement.appendChild(createField(fields[i]));

  if (addButton) {
    let buttonContainer = createQuestionCard();
    let button = document.createElement("button");
    button.setAttribute("type", "submit");
    button.textContent = "SUBMIT";
    buttonContainer.appendChild(button);
    formElement.appendChild(buttonContainer);
  }
  
  return formElement;
}

if (document.getElementById("load-form")) {
  if (formId) {
    let form = fetch("../getform/" + formId);
    form
      .then((response) => response.json())
      .then((form) => {
        document.querySelector("#loading-card").hidden = true;
        let formElement = renderForm(form, true);
        let csrfToken = document.getElementById("csrfToken");
        csrfToken.remove();
        formElement.appendChild(csrfToken);
        document.querySelector("body").appendChild(formElement);
      });
  }
}
