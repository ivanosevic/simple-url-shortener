/**
 * Until document is ready...
 */
document.addEventListener("DOMContentLoaded", function (event) {
  document.addEventListener('click', function (clickEvent) {
    qrCodeBtn(clickEvent);
    deleteUrlBtn(clickEvent);
  });
});

const qrCodeBtn = function (evt) {
  if (evt.target.matches('.qr-code')) {
    evt.preventDefault();
    let image = evt.target.getAttribute('qr-image');
    swal.fire({
      imageUrl: image,
      imageWidth: 400,
      imageHeight: 400,
      showConfirmButton: false,
      showCloseButton: true
    });
  }
};

const deleteUrlBtn = function (evt) {
  if (evt.target.matches('.delete-url')) {
    evt.preventDefault();
    let actionLink = evt.target.getAttribute('href');
    let redirectUrl = evt.target.getAttribute('data-redirect-url');
    Swal.fire({
      title: 'Are you sure you want to delete the url ' + redirectUrl,
      text: "You won't be able to revert this!",
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