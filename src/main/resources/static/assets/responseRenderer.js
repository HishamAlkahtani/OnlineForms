let testResponse = [
  {
    formId: 1,
    response: {
      Vote: "Ahmad",
      Car: "true",
      "First Name": "Hisham Alkahtani",
      Bike: "true",
    },
  },
  {
    formId: 1,
    response: { Vote: "Saud", "First Name": "Nawaf Alharbi", Bike: "true" },
  },
  {
    formId: 1,
    response: { Vote: "Ahmad", Car: "true", "First Name": "Shoka Boka" },
  },
  { formId: 1, response: { Vote: "Ahmad", "First Name": "Khalid Alzeer" } },
];

let testform = {
  formId: 1,
  formName: "TestForm",
  description: null,
  columns: ["First Name", "Vote", "Car", "Bike"],
  fields: [
    {
      title: "First Name",
      description: "The name that you were given at birth!",
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
};

function renderResponses(form, responses) {
  let columns = form.columns;

  document.querySelector("#loading-card").hidden = true;
  let table = document.createElement("table");

  let tableHeaders = document.createElement("tr");
  for (let i in columns) {
    let header = document.createElement("th");
    header.textContent = columns[i];
    tableHeaders.appendChild(header);
  }
  table.appendChild(tableHeaders);

  for (let i in responses) {
    let row = document.createElement("tr");
    for (let data in columns) {
      let cell = document.createElement("td");
      cell.textContent = responses[i].response[columns[data]];
      row.appendChild(cell);
    }
    table.appendChild(row);
  }

  document.querySelector("body").appendChild(table);
}

if (formId) {
  let form = fetch("../getform/" + formId, {credentials: "same-origin"}).then((value) => value.json());
  let responses = fetch("../responsesRaw/" + formId, {credentials: "same-origin"}).then((value) =>
    value.json()
  );

  Promise.all([form, responses]).then((values) => {
    renderResponses(values[0], values[1]);
  });
}
