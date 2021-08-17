/**
 * Until document is ready...
 */
document.addEventListener("DOMContentLoaded", function (event) {
  const clipboard = new ClipboardJS('.copyable');

  clipboard.on('success', function () {
    alertify.notify('Copied! :)', 'success', 5);
  });

  clipboard.on('error', function () {
    alertify.notify('Error copying :(', 'error', 5);
  });

  document.addEventListener('click', function (clickEvent) {
    qrCodeBtn(clickEvent);
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