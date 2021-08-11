/**
 * Until document is ready...
 */
document.addEventListener("DOMContentLoaded", function (event) {
  document.addEventListener('click', function (clickEvent) {
    grantPrivilegesBtn(clickEvent);
    removePrivilegesBtn(clickEvent);
    deleteUserBtn(clickEvent);
  });
});

const grantPrivilegesBtn = function (evt) {
  if (evt.target.matches('.grant-privileges')) {
    evt.preventDefault();
    let actionLink = evt.target.getAttribute('href');
    let user = evt.target.getAttribute('data-username');
    Swal.fire({
      title: 'Are you sure you want to grant the user ' + user + ' Admin privileges?',
      text: "Don't worry! You can revert this action later",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        location.href = actionLink;
      }
    })
  }
};

const removePrivilegesBtn = function (evt) {
  if (evt.target.matches('.remove-privileges')) {
    evt.preventDefault();
    let actionLink = evt.target.getAttribute('href');
    let user = evt.target.getAttribute('data-username');
    Swal.fire({
      title: 'Are you sure you want to remove the Admin privileges of the user ' + user + ' ?',
      text: "Don't worry! You can revert this action later",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        location.href = actionLink;
      }
    })
  }
};

const deleteUserBtn = function (evt) {
  if (evt.target.matches('.delete-user')) {
    evt.preventDefault();
    let actionLink = evt.target.getAttribute('href');
    let user = evt.target.getAttribute('data-username');
    Swal.fire({
      title: 'Are you sure you want to delete the user ' + user + ' ?',
      text: "You can't revert this action!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        location.href = actionLink;
      }
    })
  }
}